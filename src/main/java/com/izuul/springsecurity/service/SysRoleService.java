package com.izuul.springsecurity.service;

import com.izuul.springsecurity.controller.vo.RoleInfo;
import com.izuul.springsecurity.entity.SysRole;
import com.izuul.springsecurity.entity.SysRoute;
import com.izuul.springsecurity.repository.SysRoleRepository;
import com.izuul.springsecurity.repository.SysRouteRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Guihong.Zhang
 * @date 2019-07-14 14:28
 */
@Service
public class SysRoleService {

    @Autowired
    private SysRoleRepository sysRoleRepository;

    @Autowired
    private SysRouteRepository sysRouteRepository;

    /**
     * 创建role
     */
    public RoleInfo insertRole(RoleInfo roleInfo) {
        List<SysRoute> sysRoutes = new ArrayList<>();
        SysRole sysRole = new SysRole();
        BeanUtils.copyProperties(roleInfo, sysRole);
        roleInfo.getRoutes().forEach(r -> {
            Optional<SysRoute> optional = sysRouteRepository.findById(r.getId());
            optional.ifPresent(sysRoutes::add);
        });
        sysRole.setSysRoutes(sysRoutes);

        SysRole save = sysRoleRepository.save(sysRole);
        BeanUtils.copyProperties(save, roleInfo);
        roleInfo.setKey(save.getId());
        return roleInfo;
    }

    /**
     * 删除 role
     */
    public void deleteRole(String id) {
        sysRoleRepository.deleteById(id);
    }

    /**
     * 修改 role
     */
    public RoleInfo updateRole(RoleInfo roleInfo) {
        List<SysRoute> sysRoutes = new ArrayList<>();
        SysRole sysRole = new SysRole();
        BeanUtils.copyProperties(roleInfo, sysRole);

        roleInfo.getRoutes().forEach(r -> {
            Optional<SysRoute> optional = sysRouteRepository.findById(r.getId());
            optional.ifPresent(sysRoutes::add);
        });

        sysRole.setId(roleInfo.getKey());
        sysRole.setSysRoutes(sysRoutes);
        SysRole save = sysRoleRepository.save(sysRole);

        BeanUtils.copyProperties(save, roleInfo);
        roleInfo.setKey(save.getId());
        return roleInfo;
    }

    /**
     * 查询 roles
     */
    public List<RoleInfo> getRoles() {
        List<RoleInfo> roleInfoList = new ArrayList<>();
        List<SysRole> sysRoleList = sysRoleRepository.findAll();
        sysRoleList.forEach(r -> {
            RoleInfo roleInfo = new RoleInfo();
            BeanUtils.copyProperties(r, roleInfo);
            roleInfo.setKey(r.getId());
            roleInfoList.add(roleInfo);
        });
        return roleInfoList;
    }

    /**
     * 获取路由
     */
    public List<SysRoute> getRoutes() {
        return sysRouteRepository.findAll();
    }
}
