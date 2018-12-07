package com.weitian.repository;

import com.weitian.entity.SysRoleAcl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Administrator on 2018/12/7.
 */
public interface SysRoleAclRepository extends JpaRepository<SysRoleAcl,Integer> {
    public void deleteByRoleId(Integer roleId);
    public List<SysRoleAcl> findAllByRoleId (Integer roleId);


}
