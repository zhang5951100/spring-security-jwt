package com.izuul.springsecurity.service;

import com.izuul.springsecurity.controller.vo.RoleInfo;
import com.izuul.springsecurity.entity.SysRole;
import com.izuul.springsecurity.repository.SysRoleRepository;
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

    public List<RoleInfo> getRoles() {
        List<RoleInfo> roleInfoList = new ArrayList<>();
        List<SysRole> sysRoleList = sysRoleRepository.findAll();
        sysRoleList.forEach(r -> {
            RoleInfo roleInfo = new RoleInfo();
            roleInfo.setKey(r.getName())
                    .setName(r.getName())
                    .setDescription(r.getDescription());
        });
        return roleInfoList;
    }

}
