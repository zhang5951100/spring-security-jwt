package com.izuul.springsecurity.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Data
@Entity(name = "SYS_ROLE")
public class SysRole implements GrantedAuthority, Serializable {

    private static final long serialVersionUID = -1834111111498772935L;

    @Id
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @GeneratedValue(generator = "system-uuid")
    @Column(name = "ID")
    private String id;

    @Column(name = "NAME")
    private String name;

    @Override
    public String getAuthority() {
        return name;
    }
}
