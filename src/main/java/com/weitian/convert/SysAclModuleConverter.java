package com.weitian.convert;

import com.weitian.dto.SysAclModuleDto;
import com.weitian.entity.SysAclModule;
import com.weitian.form.ModuleForm;
import com.weitian.utils.SysUtils;
import org.springframework.beans.BeanUtils;

/**
 * Created by Administrator on 2018/12/5.
 */
public class SysAclModuleConverter {

    public static SysAclModule convert(SysAclModuleDto sysAclModuleDto){
        SysAclModule aclModule=new SysAclModule();
        BeanUtils.copyProperties( sysAclModuleDto,aclModule );
        aclModule.setOperator( SysUtils.getSessionUserName() );
        aclModule.setOperatorIP( SysUtils.getSessionUserIp() );
        return aclModule;
    }
    public static SysAclModuleDto convert(SysAclModule sysAclModule){
        SysAclModuleDto sysAclModuleDto=new SysAclModuleDto();
        BeanUtils.copyProperties( sysAclModule, sysAclModuleDto);
        return sysAclModuleDto;
    }

    public static SysAclModuleDto convert(ModuleForm moduleForm){
        SysAclModuleDto sysAclModuleDto=new SysAclModuleDto();
        BeanUtils.copyProperties( moduleForm, sysAclModuleDto);
        return sysAclModuleDto;
    }

}
