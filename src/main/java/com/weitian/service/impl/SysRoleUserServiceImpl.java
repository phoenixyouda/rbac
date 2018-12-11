package com.weitian.service.impl;

import com.weitian.convert.SysLogConvert;
import com.weitian.entity.SysLog;
import com.weitian.entity.SysRole;
import com.weitian.entity.SysRoleUser;
import com.weitian.entity.SysUser;
import com.weitian.enums.LogEnum;
import com.weitian.repository.SysRoleUserRepository;
import com.weitian.service.SysLogService;
import com.weitian.service.SysRoleUserService;
import com.weitian.utils.SysUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/12/3.
 */
@Service
public class SysRoleUserServiceImpl implements SysRoleUserService {



    @Autowired
    private SysRoleUserRepository roleUserRepository;
    @Autowired
    private SysLogService logService;

    @Override
    public List<SysRoleUser> findByRoleId(Integer roleId) {
        return roleUserRepository.findByRoleId( roleId );
    }


    /**
     * 新增关系
     * @param roleId
     * @param userIds
     */
    @Override
    @Transactional
    public void save(Integer roleId, List<String> userIds) {
        if(null==roleId){
            return;
        }
        if(CollectionUtils.isEmpty( userIds )){
            return;
        }

        List<SysRoleUser> roleUserList=new ArrayList<>(  );
        List<SysLog> logList=new ArrayList<>(  );
        for(String userId:userIds){
            SysRoleUser sysRoleUser=new SysRoleUser();
            sysRoleUser.setRoleId( roleId );
            sysRoleUser.setUserId( Integer.parseInt( userId) );
            sysRoleUser.setOperator( SysUtils.getSessionUserName() );
            sysRoleUser.setOperatorIP( SysUtils.getSessionUserIp() );
            roleUserList.add( sysRoleUser );
        }
        roleUserRepository.save( roleUserList );
        for(SysRoleUser roleUser:roleUserList){
            logList.add( SysLogConvert.convertForCD( roleUser.getId(),SysUtils.getJsonByObject( roleUser ),LogEnum.ROLE_USER_TABLE.getTableCode(),LogEnum.OPERATOR_SAVE.getOperatorCode() ) );
        }

        logService.save( logList );
    }

    /**
     * 根据角色id删除中间表信息
     * @param roleId
     */
    @Override
    @Transactional
    public void deleteByRoleId(Integer roleId) {
        if(null==roleId){
            return;
        }
        //查询出中间表相关信息
        List<SysRoleUser> roleUserList=roleUserRepository.findByRoleId( roleId );
        if(CollectionUtils.isEmpty( roleUserList )){
            return ;
        }
        roleUserRepository.deleteByRoleId( roleId );
        List<SysLog> logList=new ArrayList<>(  );
        for(SysRoleUser sysRoleUser:roleUserList){
            logList.add( SysLogConvert.convertForCD( sysRoleUser.getId(), SysUtils.getJsonByObject( sysRoleUser ), LogEnum.ROLE_USER_TABLE.getTableCode(),LogEnum.OPERATOR_DELETE.getOperatorCode() ) );
        }
        logService.save( logList );
    }

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
