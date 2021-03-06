package com.weitian.repository;

import com.weitian.entity.SysDept;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Administrator on 2018/11/22.
 */
public interface SysDeptRepository extends JpaRepository<SysDept,Integer> {
    public SysDept findByParentIdAndName(Integer parentId,String name);
    public List<SysDept> findByParentId(Integer parentId);
}
