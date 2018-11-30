package com.weitian.service.impl;

import com.weitian.convert.SysLogConvert;
import com.weitian.convert.SysUserConverter;
import com.weitian.dto.SysUserDto;
import com.weitian.entity.SysDept;
import com.weitian.entity.SysUser;
import com.weitian.enums.LogEnum;
import com.weitian.enums.ResultEnum;
import com.weitian.exception.ResultException;
import com.weitian.repository.SysUserRepository;
import com.weitian.service.SysDeptService;
import com.weitian.service.SysLogService;
import com.weitian.service.SysUserService;
import com.weitian.utils.SysUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/11/22.
 */
@Service
@Slf4j
public class SysUserServiceImpl implements SysUserService {
    @Autowired
    private SysUserRepository userRepository;
    @Autowired
    private SysDeptService deptService;
    @Autowired
    private SysLogService logService;


    /**
     * 新增人员
     * @param sysUserDto
     * @return
     */
    @Override
    @Transactional
    public SysUser save(SysUserDto sysUserDto) {

        SysDept sysDept= deptService.findOne(sysUserDto.getDeptId());
        if(null==sysDept){
            log.error( "【新增员工失败】,msg={},code={}",ResultEnum.DEPARTMENT_NOT_EXIST.getMsg(),ResultEnum.DEPARTMENT_NOT_EXIST.getCode() );
            throw new ResultException( ResultEnum.DEPARTMENT_NOT_EXIST );
        }

        SysUser sysUser=SysUserConverter.convert2User( sysUserDto );
        sysUser.setPassword( sysUser.getUsername() );
        sysUser.setSysDept( sysDept );
        sysUser.setOperator( SysUtils.getSessionUserName() );
        sysUser.setOperatorIP( SysUtils.getSessionUserIp() );
        sysUser=userRepository.save( sysUser);
        if(null!=sysUser.getId()){
            //更新日志
            logService.save( SysLogConvert.convertForCD( sysUser.getId(), SysUtils.getJsonByObject(  sysUser  ), LogEnum.USER_TABLE.getTableCode(),LogEnum.OPERATOR_SAVE.getOperatorCode() ) );
        }else{
            log.error( "【新增员工失败】,msg={},code={}",ResultEnum.USER_INSERT_ERROR.getMsg(),ResultEnum.USER_INSERT_ERROR.getCode() );
            throw new ResultException( ResultEnum.USER_INSERT_ERROR );
        }
        return sysUser;
    }

    /**
     * 查询所有人员信息
     * @return
     */
    @Override
    public Page<SysUser> findAll(Integer currPage,Integer pageSize,Integer departmentId) {

        SysDept sysDept=new SysDept();
        sysDept.setId( departmentId );


        List<Sort.Order> orders=new ArrayList< Order>();
        orders.add( new Order( Direction. ASC, "sysDept"));
        orders.add( new Order( Direction. ASC, "id"));

        Sort sort=new Sort(orders );
        Pageable pageable=new PageRequest(currPage ,pageSize,sort);

        return userRepository.findAllBySysDept(sysDept,pageable);
    }

    /**
     * 根据主键查询人员信息
     * @param id
     * @return
     */
    public SysUser findById(Integer id){
        if(id==null) {

            throw new ResultException( ResultEnum.REQUESTDATA_NOT_EXIST );
        }
        return userRepository.findOne( id );
    }
}
