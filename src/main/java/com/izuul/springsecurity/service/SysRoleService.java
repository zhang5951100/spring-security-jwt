package com.izuul.springsecurity.service;

import com.izuul.springsecurity.controller.vo.RoleInfo;
import com.izuul.springsecurity.entity.SysRole;
import com.izuul.springsecurity.entity.SysRoute;
import com.izuul.springsecurity.repository.SysRoleRepository;
import com.izuul.springsecurity.repository.SysRouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    public List<RoleInfo> getRoles() {
        List<RoleInfo> roleInfoList = new ArrayList<>();
        List<SysRole> sysRoleList = sysRoleRepository.findAll();
        sysRoleList.forEach(r -> {
            RoleInfo roleInfo = new RoleInfo();
            roleInfo.setName(r.getName())
                    .setDescription(r.getDescription())
                    .setRoutes(r.getSysRoutes());

            roleInfoList.add(roleInfo);
        });
        return roleInfoList;
    }

    public List<SysRoute> getRoutes() {
        return sysRouteRepository.findAll();
    }
}
