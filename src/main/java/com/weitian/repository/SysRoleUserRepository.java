package com.weitian.repository;

import com.weitian.entity.SysRoleUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Administrator on 2018/12/3.
 */
public interface SysRoleUserRepository extends JpaRepository<SysRoleUser,Integer>{
    public void deleteByUserId(Integer userId);
    public List<SysRoleUser> findByUserId(Integer userId);
}
