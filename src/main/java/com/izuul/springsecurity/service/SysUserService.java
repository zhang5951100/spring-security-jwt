package com.izuul.springsecurity.service;

import com.izuul.springsecurity.controller.vo.UserInfo;
import com.izuul.springsecurity.entity.SysRole;
import com.izuul.springsecurity.entity.SysUser;
import com.izuul.springsecurity.repository.SysRoleRepository;
import com.izuul.springsecurity.repository.SysUserRepository;
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
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class SysUserService implements UserDetailsService {

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
            SysRole roleAdmin = new SysRole();
            roleAdmin.setName("ROLE_ADMIN");

            SysUser sysAdmin = new SysUser();
            sysAdmin.setUsername("admin");
            sysAdmin.setPassword(passwordEncoder.encode("111111"));
            List<SysRole> sysRolesAdmin = new ArrayList<>();
            sysRolesAdmin.add(roleAdmin);
            sysAdmin.setSysRoles(sysRolesAdmin);

            sysRoleRepository.save(roleAdmin);
            sysUserRepository.save(sysAdmin);
        }

        if (sysUserRepository.findByUsername("user") == null) {
            SysRole roleUser = new SysRole();
            roleUser.setName("ROLE_USER");

            SysUser sysUser = new SysUser();
            sysUser.setUsername("user");
            sysUser.setPassword(passwordEncoder.encode("111111"));
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
                .setIntroduction("管理员")
                .setRoles(roles);

        return userInfo;
    }
}
