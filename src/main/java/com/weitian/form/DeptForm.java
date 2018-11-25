package com.weitian.form;

import com.weitian.entity.SysUser;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.OneToMany;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/11/25 0025.
 */
public class DeptForm {
    @NotEmpty(message = "部门名称不得为空")
    private String name;
    @NotEmpty(message="父部门不得为空")
    private Integer parentId;

    private String level;
    private Integer sort;
    private String remark;
    private String operator;
    private Date operatorTime;
    private String operatorIP;


}
