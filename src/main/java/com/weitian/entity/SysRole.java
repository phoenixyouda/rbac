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
 * 角色表
 *
 * 角色与用户：多对多
 *
 *
 * 角色与权限：多对多
 * Created by Administrator on 2018/11/22.
 */
@Entity
@Data
@DynamicUpdate
@Table(name="sys_role")
public class SysRole implements Serializable {
    private static final long serialVersionUID = 3793807254455266526L;
    @Id
    @GeneratedValue
    @Expose
    private Integer id;

    @Expose
    @Column(name="name")
    private String name;

    @Expose
    @Column(name="type")
    private Integer type;

    @Expose
    @Column(name="sort")
    private Integer sort;

    @Expose
    @Column(name="status")
    private Integer status;

    @Expose
    @Column(name="remark")
    private String remark;

    @Expose
    @Column(name="operator")
    private String operator;

    @Expose
    @Column(name="operator_time")
    private Date operatorTime;

    @Expose
    @Column(name="operator_ip")
    private String operatorIP;


    @JsonBackReference
    @ManyToMany(mappedBy = "sysRoleList")
    private List<SysUser> sysUserList;


    @JsonBackReference
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="sys_role_acl",joinColumns = {@JoinColumn(name="role_id")},inverseJoinColumns = {@JoinColumn(name="acl_id")})
    private List<SysAcl> sysAclList;
}
