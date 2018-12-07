package com.weitian.service;

import com.weitian.dto.SysAclModuleDto;
import com.weitian.dto.SysRoleDto;
import com.weitian.entity.SysAclModule;
import com.weitian.entity.SysRole;

import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2018/11/22.
 */
public interface SysRoleService {
    public List<SysRoleDto> findAll();
    public SysRole save(SysRoleDto sysRoleDto);
    public List<SysRole> findAllByName(String name);
    public SysRole update(SysRoleDto sysRoleDto);
    public void delete(Integer id);
    public SysRole findOne(Integer id);

    //根据角色设置权限checked
    public void setAclsByRole(SysRole sysRole, List<SysAclModule> sysAclModuleList);



}
