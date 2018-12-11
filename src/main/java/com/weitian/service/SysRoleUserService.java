package com.weitian.service;

import com.weitian.entity.SysRole;
import com.weitian.entity.SysRoleUser;
import com.weitian.entity.SysUser;

import java.util.List;

/**
 * Created by Administrator on 2018/12/3.
 */
public interface SysRoleUserService {
    public void deleteByUserId(Integer userId);
    public List<SysRoleUser> findByUserId(Integer userId);
    public List<SysRoleUser> findByRoleId(Integer roleId);
    public void deleteByRoleId(Integer roleId);
    public void save(Integer roleId, List<String> userIds);
}
