package com.weitian.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
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
public class SysAcl {
    @Id
    @GeneratedValue
    private Integer id;
    @Column(name="name")
    private String name;
    @Column(name="code")
    private String code;

    @ManyToOne
    @JoinColumn(name="acl_module_id")
    private SysAclModule sysAclModule;

    @Column(name="url")
    private String url;
    @Column(name="type")
    private Integer type;
    @Column(name="sort")
    private Integer sort;
    @Column(name="status")
    private Integer status;
    @Column(name="remark")
    private String remark;
    @Column(name="operator")
    private String operator;
    @Column(name="operator_time")
    private Date operatorTime;
    @Column(name="operator_ip")
    private String operatorIP;
    @ManyToMany(mappedBy = "sysAclList")
    private List<SysRole> sysRoleList;
}
