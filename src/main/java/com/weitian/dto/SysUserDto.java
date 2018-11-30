package com.weitian.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.weitian.entity.SysDept;
import com.weitian.entity.SysRole;
import com.weitian.entity.SysUser;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/11/29.
 */
@Data
public class SysUserDto extends SysUser {
    //部门名称
    private String deptName;
    //部门Id
    private Integer deptId;
    //状态名称
    private String statusName;

}
