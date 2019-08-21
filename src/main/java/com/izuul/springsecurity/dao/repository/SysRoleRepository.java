package com.izuul.springsecurity.dao.repository;

import com.izuul.springsecurity.entity.SysRole;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Guihong.Zhang
 * @date 2019-07-14 09:47
 */
public interface SysRoleRepository extends JpaRepository<SysRole, String> {
    SysRole findByName(String name);
}
