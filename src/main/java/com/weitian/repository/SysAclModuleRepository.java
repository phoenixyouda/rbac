package com.weitian.repository;

import com.weitian.entity.SysAclModule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Administrator on 2018/11/22.
 */
public interface SysAclModuleRepository extends JpaRepository<SysAclModule,Integer> {
    public SysAclModule findByParentIdAndName(Integer parentId,String name);
    public List<SysAclModule> findByParentId(Integer parentId);
}
