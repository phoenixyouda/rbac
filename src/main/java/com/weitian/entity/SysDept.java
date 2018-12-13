package com.weitian.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.google.gson.annotations.Expose;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 部门表
 * 部门与用户：一对多
 * Created by Administrator on 2018/11/22.
 */
@Entity
@Table(name="sys_dept")
@Setter
@Getter
@DynamicUpdate
public class SysDept implements  Serializable {

    private static final long serialVersionUID = 9193644645910210615L;
    @Id
    @GeneratedValue
    @Expose
    private Integer id;
    @Column(name="name")
    @Expose
    private String name;
    @Expose
    @Column(name="level")
    private String deptLevel;

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

    @JsonManagedReference
    @OneToMany(mappedBy = "sysDept",fetch = FetchType.EAGER)
    private List<SysUser> sysUserList;
}
