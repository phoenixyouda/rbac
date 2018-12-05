package com.weitian.form;

import com.weitian.entity.SysAcl;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/12/5.
 */
@Component
@Data
public class ModuleForm {
    private Integer id;

    @NotEmpty(message = "模块名称不得为空")
    private String name;
    private String moduleLevel;
    private Integer parentId=0;
    private Integer sort=0;

    private String remark="";
    private String operator;
    private Date operatorTime;
    private String operatorIP;

}
