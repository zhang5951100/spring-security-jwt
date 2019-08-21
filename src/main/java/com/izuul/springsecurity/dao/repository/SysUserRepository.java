package com.izuul.springsecurity.dao.repository;

import com.izuul.springsecurity.entity.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Guihong.Zhang
 * @date 2019-07-14 09:47
 */
public interface SysUserRepository extends JpaRepository<SysUser, String> {

    SysUser findByUsername(String username);
}
