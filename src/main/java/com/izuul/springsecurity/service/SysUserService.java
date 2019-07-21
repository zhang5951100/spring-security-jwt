package com.izuul.springsecurity.service;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.izuul.springsecurity.controller.vo.UserInfo;
import com.izuul.springsecurity.entity.SysRole;
import com.izuul.springsecurity.entity.SysRoute;
import com.izuul.springsecurity.entity.SysUser;
import com.izuul.springsecurity.exception.MyException;
import com.izuul.springsecurity.repository.SysRoleRepository;
import com.izuul.springsecurity.repository.SysUserRepository;
import com.izuul.springsecurity.util.JsonUtil;
import com.izuul.springsecurity.util.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

/**
 * @author Guihong.Zhang
 * @date 2019-07-14 09:47
 */
@Slf4j
@Service
public class SysUserService implements UserDetailsService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SysUserRepository sysUserRepository;

    @Autowired
    private SysRoleRepository sysRoleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();

    /**
     * 初始化认证用户
     */
    @PostConstruct
    private void init() {

        if (sysUserRepository.findByUsername("admin") == null) {
            String json = JsonUtil.readJsonFile("routes.json");
            JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, SysRoute.class);
            try {
                List<SysRoute> sysRouteList = objectMapper.readValue(json, javaType);

                SysRole roleAdmin = new SysRole();
                roleAdmin.setName("ROLE_ADMIN");
                roleAdmin.setDescription("系统管理员");
                roleAdmin.setSysRoutes(sysRouteList);

                SysUser sysAdmin = new SysUser();
                sysAdmin.setUsername("admin");
                sysAdmin.setPassword(passwordEncoder.encode("111111"));
                sysAdmin.setIntroduction("系统管理员");
                List<SysRole> sysRolesAdmin = new ArrayList<>();
                sysRolesAdmin.add(roleAdmin);
                sysAdmin.setSysRoles(sysRolesAdmin);

                sysRoleRepository.save(roleAdmin);
                sysUserRepository.save(sysAdmin);
            } catch (IOException e) {
                log.error("初始化route异常: {}", e.getMessage());
            }
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return sysUserRepository.findByUsername(username);
    }

    /**
     * 注册
     */
    public UserInfo addUser(UserInfo userInfo) {
        if (sysUserRepository.findByUsername(userInfo.getName()) == null) {
            SysRole sysRole = sysRoleRepository.findByName(userInfo.getRoles().get(0));
            SysUser sysUser = new SysUser()
                    .setUsername(userInfo.getName())
                    .setPassword(passwordEncoder.encode(userInfo.getPassword()))
                    .setIntroduction(userInfo.getIntroduction())
                    .setSysRoles(Collections.singletonList(sysRole));

            SysUser save = sysUserRepository.save(sysUser);
            userInfo.setKey(save.getId())
                    .setName(save.getUsername())
                    .setIntroduction(save.getIntroduction())
                    .setRoleList(save.getSysRoles());
            return userInfo;
        }
        return null;
    }

    /**
     * 获取用户信息
     */
    public UserInfo getUserInfo(HttpServletRequest req) {
        UserInfo userInfo = new UserInfo();
        List<String> roles = new ArrayList<>();

        String header = req.getHeader("Authorization");
        String authToken = header.replace("Bearer ", "");
        String username = null;
        username = jwtTokenUtil.getUsernameFromToken(authToken);

        SysUser sysUser = (SysUser) this.loadUserByUsername(username);
        sysUser.getSysRoles().forEach(r -> roles.add(r.getName()));

        userInfo.setName(sysUser.getUsername())
                .setAvatar("https://wpimg.wallstcn.com/0e03b7da-db9e-4819-ba10-9016ddfdaed3")
                .setIntroduction(sysUser.getIntroduction())
                .setRoles(roles)
                .setRoleList(sysUser.getSysRoles());

        return userInfo;
    }

    /**
     * 获取用户列表
     */
    public List<UserInfo> getUsers() {
        List<UserInfo> userInfoList = new ArrayList<>();
        List<SysUser> sysUserList = sysUserRepository.findAll();
        sysUserList.forEach(u -> {
            UserInfo userInfo = new UserInfo();
            userInfo.setKey(u.getId())
                    .setName(u.getUsername())
                    .setIntroduction(u.getIntroduction())
                    .setRoleList(u.getSysRoles());
            userInfoList.add(userInfo);
        });
        return userInfoList;
    }

    /**
     * 删除用户
     */
    public void deleteUser(String id) throws Exception {
        Optional<SysUser> optional = sysUserRepository.findById(id);
        if (optional.isPresent()) {
            SysUser sysUser = optional.get();
            String ADMIN = "admin";
            if (ADMIN.equals(sysUser.getUsername())) {
                throw new Exception();
            }
            sysUserRepository.deleteById(id);
        }
    }

    /**
     * 修改用户
     */
    public UserInfo updateUser(UserInfo userInfo) {
        Optional<SysUser> optional = sysUserRepository.findById(userInfo.getKey());
        if (optional.isPresent()) {

            List<SysRole> sysRoleList = new ArrayList<>();
            SysRole sysRole = sysRoleRepository.findByName(userInfo.getRoles().get(0));
            sysRoleList.add(sysRole);

            SysUser sysUser = optional.get();
            sysUser.setIntroduction(userInfo.getIntroduction())
                    .setSysRoles(sysRoleList);

            // 判断密码是否为空, 不为空则更新密码
            if (StringUtils.isNotBlank(userInfo.getPassword())) {
                sysUser.setPassword(passwordEncoder.encode(userInfo.getPassword()));
            }

            SysUser save = sysUserRepository.save(sysUser);
            userInfo.setKey(save.getId())
                    .setName(save.getUsername())
                    .setIntroduction(save.getIntroduction())
                    .setRoleList(save.getSysRoles());
            return userInfo;
        }
        // TODO
        // 异常处理
        return null;
    }
}
