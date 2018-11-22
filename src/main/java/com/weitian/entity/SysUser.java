package com.weitian.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**用户表
 *
 * 用户与部门表多对一关系
 *
 * 用户与角色表多对多关系
 * Created by Administrator on 2018/11/22.
 */
@Entity
@Table(name="sys_user")
@Data
@DynamicUpdate
public class SysUser {
    @Id
    @GeneratedValue
    private Integer id;
    @Column(name="username")
    private String username;
    @Column(name="password")
    private String password;
    @Column(name="mail")
    private String mail;

    @ManyToOne
    @JoinColumn(name="dept_id")
    private SysDept sysDept;
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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="sys_role_user",joinColumns = {@JoinColumn(name="user_id")},inverseJoinColumns = {@JoinColumn(name="role_id")})
    private List<SysRole> sysRoleList;

}
