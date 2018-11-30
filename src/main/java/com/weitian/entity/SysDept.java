package com.weitian.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
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
@Data
@DynamicUpdate
public class SysDept implements  Serializable {

    private static final long serialVersionUID = 9193644645910210615L;

    @Id
    @GeneratedValue
    private Integer id;
    @Column(name="name")
    private String name;
    @Column(name="level")
    private String deptLevel;
    @Column(name="parent_id")
    private Integer parentId;
    @Column(name="sort")
    private Integer sort;
    @Column(name="remark")
    private String remark;
    @Column(name="operator")
    private String operator;
    @Column(name="operator_time")
    private Date operatorTime;
    @Column(name="operator_ip")
    private String operatorIP;

    @JsonManagedReference

    @OneToMany(mappedBy = "sysDept",fetch = FetchType.EAGER)
    private List<SysUser> sysUserList;
}
