package com.weitian.form;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * Created by Administrator on 2018/12/6.
 */
@Data
public class AclForm {
    private Integer id;

    @NotEmpty(message = "权限名称不得为空")
    private String name;

    private String moduleName;
    @NotNull(message="父模块不得为空")
    private Integer moduleId;
    @NotEmpty(message="url不得为空")
    private String url;
    private Integer type;

    private Integer sort=0;
    private Integer status;
    private String remark;




}
