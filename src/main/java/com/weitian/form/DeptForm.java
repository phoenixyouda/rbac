package com.weitian.form;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.weitian.entity.SysUser;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/11/25 0025.
 */
@Component
@Data
public class DeptForm {
    @NotEmpty(message = "部门名称不得为空")
    private String name;

    private Integer id;

    private Integer parentId=0;

    private String deptLevel;


    private Integer sort=0;

    private String remark="";


    private String operator="";
    private Date operatorTime;
    private String operatorIP="";
}
