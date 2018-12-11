package com.weitian.service.impl;

import com.weitian.convert.SysLogConvert;
import com.weitian.entity.SysAcl;
import com.weitian.entity.SysLog;
import com.weitian.entity.SysRole;
import com.weitian.entity.SysRoleAcl;
import com.weitian.enums.LogEnum;
import com.weitian.enums.ResultEnum;
import com.weitian.exception.ResultException;
import com.weitian.repository.SysRoleAclRepository;
import com.weitian.service.SysLogService;
import com.weitian.service.SysRoleAclService;
import com.weitian.utils.SysUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/12/7.
 */

@Service
public class SysRoleAclServiceImpl implements SysRoleAclService {
    @Autowired
    private SysRoleAclRepository roleAclRepository;
    @Autowired
    private SysLogService logService;

    /**
     * 删除角色权限表
     * @param sysRole
     */
    @Override
    @Transactional
    public void delete(SysRole sysRole) {
        Integer roleId=sysRole.getId();
        List<SysRoleAcl> roleAclList=roleAclRepository.findAllByRoleId( roleId );
        if(CollectionUtils.isEmpty( roleAclList )){
            return;
        }
        roleAclRepository.deleteByRoleId( roleId );

        List<SysLog> logList=new ArrayList<>(  );
        for(SysRoleAcl roleAcl:roleAclList){
            logList.add( SysLogConvert.convertForCD( roleAcl.getId(), SysUtils.getJsonByObject( roleAcl ), LogEnum.ROLE_ACL_TABLE.getTableCode(),LogEnum.OPERATOR_DELETE.getOperatorCode() ) );
        }
        logService.save( logList );
    }

    /**
     * 新增角色权限表
     * @param sysRole
     * @param sysAclList
     * @return
     */

    @Override
    @Transactional
    public void save(SysRole sysRole, List<SysAcl> sysAclList) {

        if(CollectionUtils.isEmpty( sysAclList )){
            return ;
        }

        //先删除
        this.delete( sysRole );

        //后增加

        Integer roleId=sysRole.getId();
        List<SysRoleAcl> roleAclList=new ArrayList<>(  );

        for(SysAcl sysAcl:sysAclList){
            SysRoleAcl roleAcl=new SysRoleAcl();
            roleAcl.setRoleId( roleId );
            roleAcl.setAclId( sysAcl.getId() );
            roleAcl.setOperator( SysUtils.getSessionUserName() );
            roleAcl.setOperatorIP( SysUtils.getSessionUserIp() );
            roleAclList.add( roleAcl );
        }

        roleAclRepository.save(roleAclList);

        List<SysLog> logList=new ArrayList<>(  );
        for(SysRoleAcl roleAcl:roleAclList){
            logList.add( SysLogConvert.convertForCD( roleAcl.getId(),SysUtils.getJsonByObject( roleAcl ),LogEnum.ROLE_ACL_TABLE.getTableCode(),LogEnum.OPERATOR_SAVE.getOperatorCode()) );
        }
        logService.save( logList );
    }
}
