package com.weitian.convert;

import com.sun.javafx.scene.control.skin.VirtualFlow;
import com.weitian.dto.SysUserDto;
import com.weitian.entity.SysUser;
import com.weitian.enums.StatusEnum;
import com.weitian.form.UserForm;
import com.weitian.utils.SysUtils;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2018/11/29.
 */
public class SysUserConverter {
    public static SysUserDto convert(UserForm userForm){
        SysUserDto userDto=new SysUserDto();
        BeanUtils.copyProperties( userForm,userDto );
        userDto.setId( userForm.getUserId() );

        return userDto;
    }

    public static SysUserDto convert2UserDto(SysUser sysUser){
        SysUserDto sysUserDto=new SysUserDto();
        BeanUtils.copyProperties( sysUser,sysUserDto );

        sysUserDto.setDeptName( sysUser.getSysDept().getName() );

        sysUserDto.setStatusName( SysUtils.getByCode( sysUser.getStatus(), StatusEnum.class ).getMessage() );
        sysUserDto.setDeptId( sysUser.getSysDept().getId() );
        return sysUserDto;
    }
    public static List<SysUserDto> convert2UserDto(List<SysUser> sysUserList){
        return sysUserList.stream().map( e->convert2UserDto( e ) ).collect( Collectors.toList() );
    }

    public static SysUser convert2User(SysUserDto sysUserDto){
        SysUser user=new SysUser();
        BeanUtils.copyProperties( sysUserDto,user );
        return user;
    }

    public static List<SysUser> convert2User(List<SysUserDto> userDtoList){
        return userDtoList.stream().map( e->convert2User(e) ).collect( Collectors.toList() );
    }
}
