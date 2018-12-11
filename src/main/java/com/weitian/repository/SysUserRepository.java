package com.weitian.repository;

import com.weitian.entity.SysDept;
import com.weitian.entity.SysUser;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Administrator on 2018/11/22.
 */
public interface SysUserRepository extends JpaRepository<SysUser,Integer> {
    public Page<SysUser> findAllBySysDept(SysDept sysDept,Pageable pageable);
    public List<SysUser> findAllByIdIn(List<Integer> id);
}

