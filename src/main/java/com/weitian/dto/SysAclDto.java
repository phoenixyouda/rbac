package com.weitian.dto;

import com.weitian.entity.SysAcl;
import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2018/12/6.
 */
@Data
@Component
public class SysAclDto extends SysAcl {
    private String typeName;
    private String statusName;
    private String moduleName;
    private Integer moduleId;

}
