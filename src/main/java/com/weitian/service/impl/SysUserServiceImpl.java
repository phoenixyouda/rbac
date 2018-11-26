package com.weitian.service.impl;

import com.weitian.entity.SysUser;
import com.weitian.enums.ResultEnum;
import com.weitian.exception.ResultException;
import com.weitian.repository.SysUserRepository;
import com.weitian.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2018/11/22.
 */
@Service
public class SysUserServiceImpl implements SysUserService {
    @Autowired
    private SysUserRepository userRepository;
    public SysUser findById(Integer id){
        if(id==null) {

            throw new ResultException( ResultEnum.REQUESTDATA_NOT_EXIST );
        }
        return userRepository.findOne( id );
    }
}
