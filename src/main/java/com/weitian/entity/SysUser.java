package com.weitian.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
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
public class SysUser implements Serializable {

    private static final long serialVersionUID = 4359140159170892949L;
    @Id
    @GeneratedValue
    @Expose
    private Integer id;
    @Column(name="username")
    @Expose
    private String username;
    @Column(name="password")
    @Expose
    private String password;
    @Column(name="mail")
    @Expose
    private String mail;

    @Column(name="telephone")
    @Expose
    private String telephone;
    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="dept_id")
    @JsonProperty(value="sysDept")
    private SysDept sysDept;
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
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="sys_role_user",joinColumns = {@JoinColumn(name="user_id")},inverseJoinColumns = {@JoinColumn(name="role_id")})
    private List<SysRole> sysRoleList;


}
