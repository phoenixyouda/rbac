package com.weitian.dto;

import com.weitian.entity.SysAclModule;
import lombok.Data;

import java.util.List;

/**
 * Created by Administrator on 2018/12/5.
 */
@Data
public class SysAclModuleDto extends SysAclModule{
    //子模块
    private List<SysAclModuleDto> children;
    //默认打开方式
    private boolean open;
}
