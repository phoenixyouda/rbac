package com.weitian.dto;

import com.weitian.entity.SysLog;
import lombok.Data;

/**
 * Created by Administrator on 2018/12/10.
 */
@Data
public class SysLogDto extends SysLog {
    private String tableName;
    private String statusName;
    private String updateTypeName;

}
