package com.weitian.repository;

import com.weitian.entity.SysRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Administrator on 2018/11/22.
 */
public interface SysRoleRepository extends JpaRepository<SysRole,Integer> {
    public List<SysRole> findAllByName(String name);
}
