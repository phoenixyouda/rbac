package com.weitian.dto;

import com.weitian.entity.SysRole;
import lombok.Data;

/**
 * Created by Administrator on 2018/12/6.
 */
@Data
public class SysRoleDto extends SysRole {
    private String typeName;
    private String statusName;

}
