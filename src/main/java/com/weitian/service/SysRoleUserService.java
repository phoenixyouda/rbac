package com.weitian.service;

import com.weitian.entity.SysRoleUser;

import java.util.List;

/**
 * Created by Administrator on 2018/12/3.
 */
public interface SysRoleUserService {
    public void deleteByUserId(Integer userId);
    public List<SysRoleUser> findByUserId(Integer userId);
}
