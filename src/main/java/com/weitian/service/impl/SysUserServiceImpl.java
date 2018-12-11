package com.weitian.service.impl;

import com.weitian.convert.SysLogConvert;
import com.weitian.convert.SysUserConverter;
import com.weitian.dto.SysUserDto;
import com.weitian.entity.SysDept;
import com.weitian.entity.SysLog;
import com.weitian.entity.SysRoleUser;
import com.weitian.entity.SysUser;
import com.weitian.enums.LogEnum;
import com.weitian.enums.ResultEnum;
import com.weitian.exception.ResultException;
import com.weitian.repository.SysUserRepository;
import com.weitian.service.SysDeptService;
import com.weitian.service.SysLogService;
import com.weitian.service.SysRoleUserService;
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

import javax.xml.transform.Result;
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
    @Autowired
    private SysRoleUserService roleUserService;


    /**
     * 删除用户
     * @param id
     */
    @Override
    @Transactional
    public void delete(Integer id) {


        //同时删除中间表role_user数据
        //查询出待删除的用户
        SysUser sysUser=userRepository.findOne( id );

        if(null==sysUser){
            log.error( "【删除用户失败】,msg={},code={}", ResultEnum.USER_NOT_EXIST.getMsg(),ResultEnum.USER_NOT_EXIST.getCode());
            throw new ResultException( ResultEnum.USER_NOT_EXIST );
        }
        //查询出中间表数据
        List<SysRoleUser> roleUserList=roleUserService.findByUserId( id );

        //生成待删除用户日志
        SysLog sysLogUser=SysLogConvert.convertForCD( sysUser.getId(), SysUtils.getJsonByObject( sysUser ),LogEnum.USER_TABLE.getTableCode(),LogEnum.OPERATOR_DELETE.getOperatorCode());
        //生成待删除中间表日志
        List<SysLog> sysLogList=new ArrayList<>(  );
        for(SysRoleUser roleUser:roleUserList){
            SysLog sysRoleUserLog=SysLogConvert.convertForCD( roleUser.getId(),SysUtils.getJsonByObject( roleUser ),LogEnum.ROLE_USER_TABLE.getTableCode(),LogEnum.OPERATOR_DELETE.getOperatorCode() );
            sysLogList.add( sysRoleUserLog );
        }

        userRepository.delete( sysUser );
        roleUserService.deleteByUserId( id );

        logService.save( sysLogUser );
        logService.save( sysLogList );



    }

    /**
     * 修改用户
     * @param sysUserDto
     * @return
     */
    @Override
    @Transactional
    public SysUser update(SysUserDto sysUserDto) {
        SysUser oldSysUser=userRepository.findOne( sysUserDto.getId() );

        SysDept sysDept=deptService.findOne( sysUserDto.getDeptId() );

        String oldValue=SysUtils.getJsonByObject( oldSysUser );
        String newValue=SysUtils.getJsonByObject( sysUserDto );
        SysUser newSysUser=SysUserConverter.convert2User( sysUserDto );
        newSysUser.setOperator( SysUtils.getSessionUserName() );
        newSysUser.setOperatorIP( SysUtils.getSessionUserIp() );
        newSysUser.setPassword( oldSysUser.getPassword() );
        newSysUser.setSysDept( sysDept );
        SysUser updateUser=userRepository.save( newSysUser );


        if(null==updateUser){
            log.error( "【员工更新异常】,message={},code={}",ResultEnum.USER_UPDATE_ERROR.getMsg(),ResultEnum.USER_UPDATE_ERROR.getCode() );
            throw new ResultException( ResultEnum.USER_UPDATE_ERROR );
        }else{
            //更新日志
            logService.save( SysLogConvert.convertForUpdate( updateUser.getId(),oldValue,newValue,LogEnum.USER_TABLE.getTableCode() ));
        }


        return updateUser;
    }

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
            log.error( "【新增员工失败】,msg={},code={}",ResultEnum.DEPARTMENT_NOT_EXISTS.getMsg(),ResultEnum.DEPARTMENT_NOT_EXISTS.getCode() );
            throw new ResultException( ResultEnum.DEPARTMENT_NOT_EXISTS );
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

    @Override
    public List<SysUser> findAllByIdIn(List<Integer> ids) {
        return userRepository.findAllByIdIn( ids );
    }

    @Override
    public List<SysUser> findAll() {
        return userRepository.findAll();
    }

    /**
     * 分页查询所有人员信息
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

            throw new ResultException( ResultEnum.REQUESTDATA_NOT_EXISTS );
        }
        return userRepository.findOne( id );
    }
}
