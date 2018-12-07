package com.weitian.service;

import com.weitian.entity.SysAcl;
import com.weitian.entity.SysRole;
import com.weitian.entity.SysRoleAcl;

import java.util.List;

/**
 * Created by Administrator on 2018/12/7.
 */
public interface SysRoleAclService {
    public void save(SysRole sysRole, List<SysAcl> sysAclList);
    public void delete(SysRole sysRole);
}
