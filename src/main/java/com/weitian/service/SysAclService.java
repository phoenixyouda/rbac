package com.weitian.service;

import com.weitian.dto.SysAclDto;
import com.weitian.entity.SysAcl;
import com.weitian.entity.SysAclModule;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created by Administrator on 2018/11/22.
 */
public interface SysAclService {
    public Page<SysAcl> findByModuleId(Integer currPage, Integer pageSize, Integer moduleId);
    public SysAcl save(SysAclDto sysAclDto);

    public List<SysAcl> findBySysAclModuleAndName(SysAclModule sysAclModule, String name);
    public SysAclDto findById(Integer id);
    public SysAcl update(SysAclDto sysAclDto);


}
