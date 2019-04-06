package com.izuul.springsecurity.repository;

import com.izuul.springsecurity.entity.SysRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SysRoleRepository extends JpaRepository<SysRole, String> {
}
