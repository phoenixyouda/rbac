package com.weitian.entity;

import com.google.gson.annotations.Expose;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;


/**
 * Created by Administrator on 2018/12/3.
 */
@Entity
@Data
@Table(name="sys_role_user")
public class SysRoleUser {
    @Id
    @GeneratedValue
    @Expose
    private Integer id;
    @Column(name="role_id")
    @Expose
    private Integer roleId;
    @Column(name="user_id")
    @Expose
    private Integer userId;
    @Column(name="operator")
    @Expose
    private String operator;
    @Column(name="operator_time")
    @Expose
    private Date operatorTime;
    @Column(name="operator_ip")
    @Expose
    private String operatorIP;

}
