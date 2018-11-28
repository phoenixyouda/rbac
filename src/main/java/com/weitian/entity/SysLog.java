package com.weitian.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

/**
 * 操作日志表
 * Created by Administrator on 2018/11/22.
 */
@Entity
@Data
@DynamicUpdate
@Table(name="sys_log")
public class SysLog {
    @Id
    @GeneratedValue
    private Integer id;
    @Column(name="type")
    private Integer type;
    @Column(name="target_id")
    private Integer targetId;
    @Column(name="old_value")
    private String oldValue;
    @Column(name="new_value")
    private String newValue;
    @Column(name="status")
    private Integer status;
    @Column(name="update_type")
    private Integer updateType;
    @Column(name="operator")
    private String operator;
    @Column(name="operator_time")
    private Date operatorTime;
    @Column(name="operator_ip")
    private String operatorIP;
}
