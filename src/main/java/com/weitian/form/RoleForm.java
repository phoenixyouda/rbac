package com.weitian.form;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * Created by Administrator on 2018/12/6.
 */
@Data
public class RoleForm {
    private Integer roleId;
    @NotEmpty(message = "角色名称不得为空")
    private String roleName;

    private Integer status;

    private String remark;

    @NotNull(message = "顺序不得为空")
    private Integer roleSort;

    private Integer roleType;

}
