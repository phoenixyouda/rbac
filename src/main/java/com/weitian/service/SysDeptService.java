package com.weitian.service;

import com.weitian.dto.SysDeptDto;
import com.weitian.entity.SysDept;

import java.util.List;

/**
 * Created by Administrator on 2018/11/22.
 */
public interface SysDeptService {
    public SysDept  save(SysDeptDto deptDto);
    public SysDept findOne(Integer id);
    public SysDept findByParentIdAndName(Integer parentId,String name);
    public List<SysDept> findAllDept();
    public SysDept update(SysDeptDto deptDto);
    public void delete(Integer id);
    

}
