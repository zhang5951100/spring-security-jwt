package com.izuul.springsecurity.service;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.izuul.springsecurity.controller.vo.UserInfo;
import com.izuul.springsecurity.entity.SysRole;
import com.izuul.springsecurity.entity.SysRoute;
import com.izuul.springsecurity.entity.SysUser;
import com.izuul.springsecurity.repository.SysRoleRepository;
import com.izuul.springsecurity.repository.SysUserRepository;
import com.izuul.springsecurity.util.JsonUtil;
import com.izuul.springsecurity.util.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
     * 初始化2个认证用户
     */
    @PostConstruct
    private void init() {

        if (sysUserRepository.findByUsername("admin") == null) {
            String json = JsonUtil.readJsonFile("constantRoutes.json");
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

        if (sysUserRepository.findByUsername("user") == null) {
            SysRole roleUser = new SysRole();
            roleUser.setName("ROLE_USER");
            roleUser.setDescription("普通用户");
            roleUser.setSysRoutes(new ArrayList<>());

            SysUser sysUser = new SysUser();
            sysUser.setUsername("user");
            sysUser.setPassword(passwordEncoder.encode("111111"));
            sysUser.setIntroduction("普通用户");
            List<SysRole> sysRolesUser = new ArrayList<>();
            sysRolesUser.add(roleUser);
            sysUser.setSysRoles(sysRolesUser);

            sysRoleRepository.save(roleUser);
            sysUserRepository.save(sysUser);

        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return sysUserRepository.findByUsername(username);
    }

    /**
     * 注册
     */
    public SysUser register(SysUser sysUser) {
        if (sysUserRepository.findByUsername(sysUser.getUsername()) == null) {
            SysRole roleUser = sysRoleRepository.findByName("ROLE_USER");

            sysUser.setPassword(passwordEncoder.encode(sysUser.getPassword()));
            List<SysRole> sysRolesUser = new ArrayList<>();
            sysRolesUser.add(roleUser);
            sysUser.setSysRoles(sysRolesUser);

            SysUser user = sysUserRepository.save(sysUser);
            user.setPassword(null);
            return user;
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
        String username = jwtTokenUtil.getUsernameFromToken(authToken);

        SysUser sysUser = (SysUser) this.loadUserByUsername(username);
        sysUser.getSysRoles().forEach(r -> roles.add(r.getName()));

        userInfo.setName(sysUser.getUsername())
                .setAvatar("https://wpimg.wallstcn.com/0e03b7da-db9e-4819-ba10-9016ddfdaed3")
                .setIntroduction(sysUser.getIntroduction())
                .setRoles(roles);

        return userInfo;
    }
}
