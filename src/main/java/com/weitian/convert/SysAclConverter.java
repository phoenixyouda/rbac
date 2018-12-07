package com.weitian.convert;

import com.weitian.dto.SysAclDto;
import com.weitian.entity.SysAcl;
import com.weitian.enums.StatusEnum;
import com.weitian.enums.TypeEnum;
import com.weitian.form.AclForm;
import com.weitian.utils.SysUtils;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2018/12/6.
 */
public class SysAclConverter {
    public static SysAcl convert(SysAclDto sysAclDto){
        SysAcl sysAcl=new SysAcl();
        BeanUtils.copyProperties( sysAclDto,sysAcl );
        sysAcl.setOperator( SysUtils.getSessionUserName() );
        sysAcl.setOperatorIP( SysUtils.getSessionUserIp() );

        return sysAcl;
    }

    public static SysAclDto convert(SysAcl sysAcl){
        SysAclDto sysAclDto=new SysAclDto();
        BeanUtils.copyProperties( sysAcl,sysAclDto );
        sysAclDto.setStatusName( SysUtils.getByCode(sysAclDto.getStatus(),StatusEnum.class ).getMessage() );
        sysAclDto.setTypeName( SysUtils.getByCode( sysAclDto.getType(), TypeEnum.class ).getMsg() );
        sysAclDto.setModuleName( sysAcl.getSysAclModule().getName() );
        sysAclDto.setModuleId( sysAcl.getSysAclModule().getId() );
        return sysAclDto;
    }

    public static List<SysAclDto> convert(List<SysAcl> sysAclList){
        return sysAclList.stream().map( e->convert(e) ).collect( Collectors.toList() );
    }

    public static SysAclDto convert(AclForm aclForm){
        SysAclDto sysAclDto=new SysAclDto();
        BeanUtils.copyProperties( aclForm,sysAclDto );
        return sysAclDto;
    }

}
