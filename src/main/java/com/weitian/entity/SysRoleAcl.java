package com.weitian.entity;

import com.google.gson.annotations.Expose;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2018/12/7.
 */
@Table(name="sys_role_acl")
@Entity
@Data
@DynamicUpdate
public class SysRoleAcl implements Serializable {
    private static final long serialVersionUID = 2387844862817910657L;
    @Id
    @GeneratedValue
    @Expose
    private Integer id;
    @Column(name="acl_id")
    @Expose
    private Integer aclId;
    @Expose
    @Column(name="role_id")
    private Integer roleId;

    @Expose
    @Column(name="operator")
    private String operator;

    @Expose
    @Column(name="operator_time")
    private Date operatorTime;

    @Expose
    @Column(name="operator_ip")
    private String operatorIP;
}
