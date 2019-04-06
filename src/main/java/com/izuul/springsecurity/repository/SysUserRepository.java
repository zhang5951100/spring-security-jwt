package com.izuul.springsecurity.repository;

import com.izuul.springsecurity.entity.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SysUserRepository extends JpaRepository<SysUser, String> {

    SysUser findByUsername(String username);
}
