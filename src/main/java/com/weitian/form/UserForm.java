package com.weitian.form;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by Administrator on 2018/11/30.
 */
@Data
public class UserForm {
    @NotEmpty(message="部门不得为空")
    private String deptName;
    private Integer deptId;
    @NotEmpty(message="姓名不得为空")
    private String username;
    private Integer userId;
    @NotEmpty(message="邮箱不得为空")
    private String mail;
    @NotEmpty(message = "电话不得为空")
    private String telephone;

    private Integer status;
    private String remark;
}
