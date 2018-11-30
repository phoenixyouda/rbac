package com.weitian.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
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
public class SysRole {
    @Id
    @GeneratedValue
    private Integer id;
    @Column(name="name")
    private String name;
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
    @JsonBackReference
    @ManyToMany(mappedBy = "sysRoleList")
    private List<SysUser> sysUserList;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="sys_role_acl",joinColumns = {@JoinColumn(name="role_id")},inverseJoinColumns = {@JoinColumn(name="acl_id")})
    private List<SysAcl> sysAclList;
}
