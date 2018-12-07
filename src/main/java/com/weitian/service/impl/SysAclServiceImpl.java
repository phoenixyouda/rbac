package com.weitian.service.impl;

import com.weitian.convert.SysAclConverter;
import com.weitian.convert.SysLogConvert;
import com.weitian.dto.SysAclDto;
import com.weitian.entity.SysAcl;
import com.weitian.entity.SysAclModule;
import com.weitian.enums.LogEnum;
import com.weitian.enums.ResultEnum;
import com.weitian.exception.ResultException;
import com.weitian.repository.SysAclRepository;
import com.weitian.service.SysAclModuleService;
import com.weitian.service.SysAclService;
import com.weitian.service.SysLogService;
import com.weitian.utils.SysUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/11/22.
 */
@Service
@Slf4j
public class SysAclServiceImpl implements SysAclService{

    @Autowired
    private SysAclModuleService moduleService;
    @Autowired
    private SysAclRepository aclRepository;
    @Autowired
    private SysLogService logService;


    /**
     * 修改权限点
     * @param sysAclDto
     * @return
     */
    @Override
    @Transactional
    public SysAcl update(SysAclDto sysAclDto) {
        //查出原值
        SysAcl oldAcl=aclRepository.findOne( sysAclDto.getId() );
        if(null==oldAcl){
            log.error( "【权限管理】,msg={},code={}",ResultEnum.ACL_NOT_EXISTS.getMsg(),ResultEnum.ACL_NOT_EXISTS.getCode() );
            throw new ResultException( ResultEnum.ACL_NOT_EXISTS );
        }
        //生成oldvalue
        String oldValue=SysUtils.getJsonByObject( oldAcl );
        //构建新值
        SysAcl newAcl=SysAclConverter.convert( sysAclDto );

        SysAclModule sysAclModule=moduleService.findOne( sysAclDto.getModuleId() );
        if(null==sysAclModule){
            log.error( "【权限管理】,msg={},code={}",ResultEnum.ACL_MODULE_NOT_EXISTS.getMsg(),ResultEnum.ACL_MODULE_NOT_EXISTS.getCode() );
            throw new ResultException( ResultEnum.ACL_MODULE_NOT_EXISTS );
        }
        newAcl.setSysAclModule( sysAclModule );
        newAcl.setCode( oldAcl.getCode() );
        //执行修改
        newAcl=aclRepository.save( newAcl );
        if(null==newAcl){
            log.error( "【权限管理】,msg={},code={}",ResultEnum.ACL_UPDATE_ERROR.getMsg(),ResultEnum.ACL_UPDATE_ERROR.getCode() );
            throw new ResultException( ResultEnum.ACL_UPDATE_ERROR );
        }
        String newValue=SysUtils.getJsonByObject( newAcl );

        //修改后检查名称是否重复
        List<SysAcl> aclList=aclRepository.findAllBySysAclModuleAndName( newAcl.getSysAclModule(),newAcl.getName() );
        if(aclList.size()>1){
            log.error( "【修改权限点异常】,msg={},code={}",ResultEnum.ACL_IS_EXISTS.getMsg(),ResultEnum.ACL_IS_EXISTS.getCode() );
            throw new ResultException( ResultEnum.ACL_IS_EXISTS );
        }else{
            //增加操作日志
            logService.save( SysLogConvert.convertForUpdate( newAcl.getId(),oldValue,newValue,LogEnum.ACL_TABLE.getTableCode() ) );
        }
        return newAcl;
    }

    /**
     * 新增权限点
     * @param sysAclDto
     * @return
     */
    @Override
    @Transactional
    public SysAcl save(SysAclDto sysAclDto) {

        //检查相同权限组下权限点名称不得重复
        SysAclModule sysAclModule=moduleService.findOne( sysAclDto.getModuleId() );
        if(null==sysAclModule){
            log.error( "【查询权限模块异常】,msg={},code={}",ResultEnum.ACL_MODULE_NOT_EXISTS.getMsg(),ResultEnum.ACL_MODULE_NOT_EXISTS.getCode() );
            throw new ResultException( ResultEnum.ACL_MODULE_NOT_EXISTS );
        }

        if(this.findBySysAclModuleAndName( sysAclModule,sysAclDto.getName() ).size()>0){
            log.error( "【新增权限点异常】,msg={},code={}",ResultEnum.ACL_IS_EXISTS.getMsg(),ResultEnum.ACL_IS_EXISTS.getCode() );
            throw new ResultException( ResultEnum.ACL_IS_EXISTS );
        }
        //生成唯一code码
        sysAclDto.setCode( SysUtils.getCodeForAcl() );
        SysAcl sysAcl=SysAclConverter.convert( sysAclDto );
        sysAcl.setSysAclModule( sysAclModule );
        //保存
        sysAcl=aclRepository.save( sysAcl );

        if(null==sysAcl){
            log.error( "【权限管理】,msg={},code={}",ResultEnum.ACL_INSERT_ERROR.getMsg(),ResultEnum.ACL_INSERT_ERROR.getCode() );
            throw new ResultException( ResultEnum.ACL_INSERT_ERROR );
        }else{
            //新增日志
            logService.save( SysLogConvert.convertForCD( sysAcl.getId(),SysUtils.getJsonByObject( sysAcl ), LogEnum.ACL_TABLE.getTableCode(),LogEnum.OPERATOR_SAVE.getOperatorCode() ) );
        }

        return sysAcl;
    }

    @Override
    public List<SysAcl> findBySysAclModuleAndName(SysAclModule sysAclModule, String name) {

        return aclRepository.findAllBySysAclModuleAndName( sysAclModule,name );
    }

    /**
     * 查询所有权限点
     * @param currPage
     * @param pageSize
     * @param moduleId
     * @return
     */
    @Override
    public Page<SysAcl> findByModuleId(Integer currPage,Integer pageSize, Integer moduleId) {

        SysAclModule sysAclModule = moduleService.findOne( moduleId );

        if(null==sysAclModule){
            log.error( "【权限模块不存在】，msg={},code={}" , ResultEnum.ACL_MODULE_NOT_EXISTS.getMsg(),ResultEnum.ACL_MODULE_NOT_EXISTS.getCode());
            throw new ResultException( ResultEnum.ACL_MODULE_NOT_EXISTS );
        }
        List<Sort.Order> orders=new ArrayList<>(  );

        orders.add( new Sort.Order( Sort.Direction. ASC, "sort"));
        orders.add( new Sort.Order( Sort.Direction. ASC, "type"));
        orders.add( new Sort.Order( Sort.Direction. ASC, "status"));
        orders.add( new Sort.Order( Sort.Direction. ASC, "name"));
        Sort sort=new Sort( orders );
        Pageable pageable=new PageRequest( currPage,pageSize, sort );
        return aclRepository.findAllBySysAclModule( pageable,sysAclModule );
    }

    @Override
    public SysAclDto findById(Integer id) {
        SysAcl sysAcl=aclRepository.findOne( id );
        if(null==sysAcl){
            log.error( "【{}】,code={},id={}",ResultEnum.ACL_NOT_EXISTS.getMsg(),ResultEnum.ACL_NOT_EXISTS.getCode(),id );
            throw new ResultException( ResultEnum.ACL_NOT_EXISTS );
        }
        return SysAclConverter.convert( sysAcl );
    }
}
