package com.weitian.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * 权限模块表
 * 权限模块与权限：一对多
 * Created by Administrator on 2018/11/22.
 */
@Entity
@Table(name="sys_acl_module")
@DynamicUpdate
@Data
public class SysAclModule {
    @Id
    @GeneratedValue
    private Integer id;
    @Column(name="name")
    private String name;
    @Column(name="level")
    private String level;
    @Column(name="parent_id")
    private Integer parentId;
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

    @OneToMany(mappedBy = "sysAclModule")
    private List<SysAcl> sysAclList;
}
