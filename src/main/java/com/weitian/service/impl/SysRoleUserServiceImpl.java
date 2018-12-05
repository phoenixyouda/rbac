package com.weitian.service.impl;

import com.weitian.entity.SysRoleUser;
import com.weitian.repository.SysRoleUserRepository;
import com.weitian.service.SysRoleUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2018/12/3.
 */
@Service
public class SysRoleUserServiceImpl implements SysRoleUserService {
    @Autowired
    private SysRoleUserRepository roleUserRepository;
    /**
     * 根据用户Id删除
     * @param userId
     */
    @Override
    public void deleteByUserId(Integer userId) {
        roleUserRepository.deleteByUserId( userId );
    }

    /**
     * 根据用户id查询
     * @param userId
     * @return
     */
    @Override
    public List<SysRoleUser> findByUserId(Integer userId) {
        return roleUserRepository.findByUserId( userId );
    }
}
