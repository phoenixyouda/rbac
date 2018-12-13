package com.weitian.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.google.gson.annotations.Expose;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 权限表
 * 权限与权限模块：多对一
 * 权限与角色：多对多
 * Created by Administrator on 2018/11/22.
 */
@Data
@Entity
@Table(name="sys_acl")
@DynamicUpdate
public class SysAcl implements Serializable {
    private static final long serialVersionUID = -6486750804462405216L;
    @Id
    @GeneratedValue
    @Expose
    private Integer id;

    @Expose
    @Column(name="name")
    private String name;

    @Expose
    @Column(name="code")
    private String code;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name="acl_module_id")
    private SysAclModule sysAclModule;

    @Column(name="url")
    @Expose
    private String url;

    @Column(name="type")
    @Expose
    private Integer type;

    @Column(name="sort")
    @Expose
    private Integer sort;

    @Column(name="status")
    @Expose
    private Integer status;

    @Column(name="remark")
    @Expose
    private String remark;

    @Column(name="operator")
    @Expose
    private String operator;

    @Column(name="operator_time")
    @Expose
    private Date operatorTime;

    @Column(name="operator_ip")
    @Expose
    private String operatorIP;

    @JsonBackReference
    @ManyToMany(mappedBy = "sysAclList")
    private List<SysRole> sysRoleList;

    @Transient
    private boolean checked;

}
