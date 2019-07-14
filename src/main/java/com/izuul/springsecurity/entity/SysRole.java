package com.izuul.springsecurity.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * @author Guihong.Zhang
 * @date 2019-07-14 09:47
 */
@Data
@Entity(name = "SYS_ROLE")
public class SysRole implements GrantedAuthority, Serializable {

    private static final long serialVersionUID = -1834111111498772935L;

    @Id
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @GeneratedValue(generator = "system-uuid")
    @Column(name = "ID", length = 40)
    private String id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @ManyToMany(cascade={CascadeType.PERSIST,CascadeType.MERGE})
    @JoinTable(name = "ROLE_ROUTE", joinColumns = @JoinColumn(name = "ROLE_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "ROUTE_ID", referencedColumnName = "ID"))
    private List<SysRoute> sysRoutes;

    @Override
    public String getAuthority() {
        return name;
    }
}
