package com.weitian.repository;

import com.weitian.entity.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Administrator on 2018/11/22.
 */
public interface SysUserRepository extends JpaRepository<SysUser,Integer> {
}
