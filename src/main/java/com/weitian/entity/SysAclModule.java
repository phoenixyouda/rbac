package com.weitian.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.google.gson.annotations.Expose;
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
    @Expose
    private Integer id;
    @Column(name="name")
    @Expose
    private String name;
    @Column(name="level")
    @Expose
    private String moduleLevel;
    @Column(name="parent_id")
    @Expose
    private Integer parentId;
    @Column(name="sort")
    @Expose
    private Integer sort;

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

    @OneToMany(mappedBy = "sysAclModule")
    @JsonManagedReference
    private List<SysAcl> sysAclList;
}
