package com.izuul.springsecurity.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.List;

/**
 * @author Guihong.Zhang
 * @date 2019-07-14 14:14
 */
@Data
@Entity(name = "SYS_ROUTE")
public class SysRoute implements Serializable {
    private static final long serialVersionUID = 563998596703990469L;

    @Id
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @GeneratedValue(generator = "system-uuid")
    @Column(name = "ID", length = 40)
    private String id;

    @Column(name = "PATH")
    private String path;

    @Column(name = "component")
    private String component;

    @Column(name = "hidden")
    private boolean hidden;

//    private List<SysRoute> children;
}
