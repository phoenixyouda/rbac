package com.weitian.convert;

import com.weitian.dto.SysRoleDto;
import com.weitian.entity.SysRole;
import com.weitian.form.RoleForm;
import com.weitian.utils.SysUtils;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2018/12/6.
 */
public class SysRoleConverter {
    public static SysRoleDto convert(SysRole role){
        SysRoleDto roleDto=new SysRoleDto();
        BeanUtils.copyProperties( role,roleDto );
        return roleDto;
    }
    public static List<SysRoleDto> convert(List<SysRole> roleList){
        return roleList.stream().map( e->convert(e) ).collect( Collectors.toList() );
    }

    public static SysRoleDto convert(RoleForm roleForm){
        SysRoleDto roleDto=new SysRoleDto();
        roleDto.setId( roleForm.getRoleId() );
        roleDto.setName( roleForm.getRoleName() );
        roleDto.setStatus(roleForm.getStatus()  );
        roleDto.setRemark( roleForm.getRemark() );
        roleDto.setSort( roleForm.getRoleSort() );
        roleDto.setType( roleForm.getRoleType() );
        return roleDto;
    }
    public static SysRole convert(SysRoleDto roleDto){
        SysRole sysRole=new SysRole();
        BeanUtils.copyProperties( roleDto,sysRole );
        sysRole.setOperator( SysUtils.getSessionUserName() );
        sysRole.setOperatorIP(SysUtils.getSessionUserIp());
        return sysRole;
    }
}
