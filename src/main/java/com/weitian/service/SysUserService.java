package com.weitian.service;

import com.weitian.dto.SysUserDto;
import com.weitian.entity.SysUser;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created by Administrator on 2018/11/22.
 */
public interface SysUserService {
    public SysUser findById(Integer id);
    public Page<SysUser> findAll(Integer currPage,Integer pageSize,Integer departmentId);
    public List<SysUser> findAll();

    public List<SysUser> findAllByIdIn(List<Integer> ids);
    public SysUser save(SysUserDto sysUserDto);
    public SysUser update(SysUserDto sysUserDto);

    public void delete(Integer id);
}
