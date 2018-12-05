package com.weitian.service;

import com.weitian.dto.SysAclModuleDto;
import com.weitian.entity.SysAcl;
import com.weitian.entity.SysAclModule;

import java.util.List;

/**
 * Created by Administrator on 2018/11/22.
 */
public interface SysAclModuleService {
    public List<SysAclModule> findAllAclModule();
    public SysAclModule save(SysAclModuleDto sysAclModuleDto);
    public SysAclModule findByParentIdAndName(Integer parentId,String name);
    public SysAclModule update(SysAclModuleDto sysAclModuleDto);
    public void delete(Integer id);
    public SysAclModule findOne(Integer id);
    public List<SysAclModule> findByParentId(Integer parentId);
}
