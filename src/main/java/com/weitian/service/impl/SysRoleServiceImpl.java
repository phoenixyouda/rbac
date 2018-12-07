package com.weitian.service.impl;

import com.weitian.convert.SysLogConvert;
import com.weitian.convert.SysRoleConverter;
import com.weitian.dto.SysAclModuleDto;
import com.weitian.dto.SysRoleDto;
import com.weitian.entity.SysAcl;
import com.weitian.entity.SysAclModule;
import com.weitian.entity.SysRole;
import com.weitian.entity.SysUser;
import com.weitian.enums.LogEnum;
import com.weitian.enums.ResultEnum;
import com.weitian.exception.ResultException;
import com.weitian.repository.SysRoleRepository;
import com.weitian.service.SysLogService;
import com.weitian.service.SysRoleService;
import com.weitian.utils.SysUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2018/11/22.
 */
@Service
@Slf4j
public class SysRoleServiceImpl implements SysRoleService {
    @Autowired
    private SysRoleRepository roleRepository;
    @Autowired
    private SysLogService logService;









    /**
     * 删除角色
     * @param id
     */
    @Override
    @Transactional
    public void delete(Integer id) {
        //查询角色
        SysRole sysRole=roleRepository.findOne( id );

        if(null==sysRole){
            throw new ResultException( ResultEnum.ROLE_NOT_EXISTS );
        }
        //检查是否在用
        List<SysAcl> aclList=sysRole.getSysAclList();
        if(aclList.size()>0){
            throw new ResultException( ResultEnum.ROLE_IS_USED );
        }
        List<SysUser> userList=sysRole.getSysUserList();
        if(userList.size()>0){
            throw new ResultException( ResultEnum.ROLE_IS_USED );
        }
        String oldValue=SysUtils.getJsonByObject( sysRole );
        //未被使用，可删除
        roleRepository.delete( sysRole );
        logService.save( SysLogConvert.convertForCD( id,oldValue,LogEnum.ROLE_TABLE.getTableCode(),LogEnum.OPERATOR_DELETE.getOperatorCode() ) );
    }

    /**
     * 修改角色
     * @param sysRoleDto
     * @return
     */
    @Override
    @Transactional
    public SysRole update(SysRoleDto sysRoleDto) {
        //查询原值
        SysRole oldRole=roleRepository.findOne( sysRoleDto.getId() );
        if(null==oldRole){
            log.error( "【更新异常】,msg={},code={}", ResultEnum.ROLE_NOT_EXISTS.getMsg(),ResultEnum.ROLE_NOT_EXISTS.getCode());
            throw new ResultException( ResultEnum.ROLE_NOT_EXISTS );
        }
        //生成oldValue
        String oldValue=SysUtils.getJsonByObject( oldRole );

        SysRole newRole=SysRoleConverter.convert( sysRoleDto );
        //执行修改
        newRole=roleRepository.save( newRole );
        //修改后检查重名
        List<SysRole> sysRoleList=roleRepository.findAllByName( newRole.getName() );

        if(sysRoleList.size()>1){
            log.error( "【更新异常】,msg={},code={}", ResultEnum.ROLE_IS_EXISTS.getMsg(),ResultEnum.ROLE_IS_EXISTS.getCode());
            throw new ResultException( ResultEnum.ROLE_IS_EXISTS );
        }else{
            String newValue=SysUtils.getJsonByObject( newRole );
            //写入日志
            logService.save( SysLogConvert.convertForUpdate( newRole.getId(), oldValue,newValue,LogEnum.ROLE_TABLE.getTableCode()) );
        }
        return newRole;
    }

    /**
     * 新增角色
     * @param sysRoleDto
     * @return
     */
    @Override
    @Transactional
    public SysRole save(SysRoleDto sysRoleDto) {
        //检查重名
        List<SysRole> roleList=this.findAllByName( sysRoleDto.getName() );
        if(roleList.size()>0){
            log.error( "【新增角色异常】,msg={},code={}", ResultEnum.ROLE_IS_EXISTS.getMsg(),ResultEnum.ROLE_IS_EXISTS.getCode());
            throw new ResultException( ResultEnum.ROLE_IS_EXISTS );
        }
        SysRole sysRole=roleRepository.save( SysRoleConverter.convert( sysRoleDto ) );
        if(null==sysRole){
            log.error( "【新增角色异常】,msg={},code={}", ResultEnum.ROLE_INSERT_ERROR.getMsg(),ResultEnum.ROLE_INSERT_ERROR.getCode());
            throw new ResultException( ResultEnum.ROLE_INSERT_ERROR );
        }else{
            logService.save( SysLogConvert.convertForCD( sysRole.getId(), SysUtils.getJsonByObject( sysRole ), LogEnum.ROLE_TABLE.getTableCode(),LogEnum.OPERATOR_SAVE.getOperatorCode() ) );
        }
        return sysRole;
    }

    /**
     * 查询所有角色
     *
     * @return
     */
    @Override
    public List<SysRoleDto> findAll() {

        List<Sort.Order> orderList=new ArrayList<>(  );
        orderList.add( new Sort.Order( Sort.Direction.ASC,"sort" ) );
        Sort sort=new Sort(orderList);
        List<SysRole> roleList=roleRepository.findAll(sort);
        return SysRoleConverter.convert( roleList );
    }

    /**
     * 查询同名的角色
     * @param name
     * @return
     */
    @Override
    public List<SysRole> findAllByName(String name) {
        return roleRepository.findAllByName( name );
    }

    /**
     * 主键查询
     * @param id
     * @return
     */
    @Override
    public SysRole findOne(Integer id) {
        return roleRepository.findOne( id );
    }


    /**
     * 将所有权限组中的权限，根据角色包含的权限设置为true
     * @param sysRole
     * @param sysAclModuleList
     */
    @Override
    public void setAclsByRole(SysRole sysRole, List<SysAclModule> sysAclModuleList) {
        List<SysAcl> roleAclList=sysRole.getSysAclList();
        for(SysAcl roleAcl:roleAclList){
            for(SysAclModule aclModule:sysAclModuleList){
                List<SysAcl> moduleAclList=aclModule.getSysAclList();
                for(SysAcl moduleAcl:moduleAclList){
                    if(roleAcl.getId().equals( moduleAcl.getId() )){
                        moduleAcl.setChecked( true );
                        aclModule.setChecked( true );
                    }
                }
            }
        }
    }
}
