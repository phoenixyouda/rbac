package com.weitian.repository;

import com.weitian.entity.SysAcl;
import com.weitian.entity.SysAclModule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Administrator on 2018/11/22.
 */
public interface SysAclRepository extends JpaRepository<SysAcl,Integer>{
    public Page<SysAcl> findAllBySysAclModule(Pageable pageable, SysAclModule sysAclModule);
    public List<SysAcl> findAllBySysAclModuleAndName(SysAclModule sysAclModule,String name);
}
