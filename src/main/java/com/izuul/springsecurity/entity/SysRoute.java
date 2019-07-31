package com.izuul.springsecurity.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.izuul.springsecurity.util.converter.ChildrenConverter;
import com.izuul.springsecurity.util.converter.MetaConverter;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * @author Guihong.Zhang
 * @date 2019-07-14 14:14
 */
@Data
@Accessors(chain = true)
@Entity(name = "SYS_ROUTE")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysRoute implements Serializable {
    private static final long serialVersionUID = 563998596703990469L;

    @Id
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @GeneratedValue(generator = "system-uuid")
    @Column(name = "ID", length = 40)
    private String id;

    @Column(name = "PATH")
    private String path;

    @Column(name = "COMPONENT")
    private String component;

    @Column(name = "REDIRECT")
    private String redirect;

    @Column(name = "ALWAYS_SHOW")
    private boolean alwaysShow;

    @Column(name = "HIDDEN")
    private boolean hidden;

    @Convert(converter = MetaConverter.class)
    @Column(name = "META",length = 1024)
    private Meta meta;

    @Convert(converter = ChildrenConverter.class)
    @Column(name = "CHILDREN",length = 2048)
    private List<Child> children;
}
