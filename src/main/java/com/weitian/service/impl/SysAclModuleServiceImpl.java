package com.weitian.service.impl;

import com.weitian.config.SysConfig;
import com.weitian.convert.SysAclModuleConverter;
import com.weitian.convert.SysLogConvert;
import com.weitian.dto.SysAclModuleDto;
import com.weitian.entity.SysAclModule;
import com.weitian.enums.LogEnum;
import com.weitian.enums.ResultEnum;
import com.weitian.exception.ResultException;
import com.weitian.repository.SysAclModuleRepository;
import com.weitian.service.SysAclModuleService;
import com.weitian.service.SysAclService;
import com.weitian.service.SysLogService;
import com.weitian.utils.LevelUtil;
import com.weitian.utils.SysUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
public class SysAclModuleServiceImpl implements SysAclModuleService {
    @Autowired
    private SysAclModuleRepository moduleRepository;
    @Autowired
    private SysLogService logService;


    /**
     * 删除模块
     * @param id
     */
    @Override
    public void delete(Integer id) {
        SysAclModule sysAclModule=moduleRepository.findOne( id );
        String oldValue=SysUtils.getJsonByObject( sysAclModule );
        //查询待删除模块是否存在
        if(null==sysAclModule){
            log.error( "【删除模块异常】,msg={},code={}",ResultEnum.ACL_MODULE_DELETE_ERROR.getMsg(),ResultEnum.ACL_MODULE_DELETE_ERROR.getCode() );
            throw new ResultException( ResultEnum.ACL_MODULE_DELETE_ERROR );
        }
        //查询待删除模块下是否有权限点
        if(sysAclModule.getSysAclList().size()>0){
            log.error( "【删除模块异常】,msg={},code={}",ResultEnum.MODULE_CONTAINS_ACLS.getMsg(),ResultEnum.MODULE_CONTAINS_ACLS.getCode() );
            throw new ResultException( ResultEnum.MODULE_CONTAINS_ACLS );
        }

        //查询待删除模块下是否有子模块
        List<SysAclModule> sysAclModuleList=this.findByParentId( sysAclModule.getId() );
        if(sysAclModuleList.size()>0){
            log.error( "【删除模块异常】,msg={},code={}",ResultEnum.MODULE_CONTAINS_CHILDMODULES.getMsg(),ResultEnum.MODULE_CONTAINS_CHILDMODULES.getCode() );
            throw new ResultException( ResultEnum.MODULE_CONTAINS_CHILDMODULES );
        }

        /*执行删除*/
        moduleRepository.delete( sysAclModule );
        logService.save( SysLogConvert.convertForCD( id,oldValue,LogEnum.ACL_MODULE_TABLE.getTableCode(),LogEnum.OPERATOR_DELETE.getOperatorCode() ) );
    }

    /**
     * 修改模块
     * @param sysAclModuleDto
     * @return
     */
    @Override
    @Transactional
    public SysAclModule update(SysAclModuleDto sysAclModuleDto) {
        SysAclModule oldAclModule=moduleRepository.findOne( sysAclModuleDto.getId() );
        String oldValue=SysUtils.getJsonByObject( oldAclModule );

        SysAclModule newAclModule=SysAclModuleConverter.convert( sysAclModuleDto );
        String newValue=SysUtils.getJsonByObject( newAclModule );

        //查询是否重名
        if (null != this.findByParentIdAndName( newAclModule.getParentId(), newAclModule.getName() )) {
            log.error( "【修改模块异常】,msg={},code={}", ResultEnum.ACL_MODULE_IS_EXISTS.getMsg(), ResultEnum.ACL_MODULE_IS_EXISTS.getCode() );
            throw new ResultException( ResultEnum.ACL_MODULE_IS_EXISTS );
        }
        //执行修改
        SysAclModule sysAclModule=moduleRepository.save( newAclModule );
        if(null==sysAclModule){
            log.error( "【修改模块异常】,msg={},code={}", ResultEnum.ACL_MODULE_UPDATE_ERROR.getMsg(),ResultEnum.ACL_MODULE_UPDATE_ERROR.getCode() );
            throw new ResultException( ResultEnum.ACL_MODULE_UPDATE_ERROR );
        }else{
            logService.save( SysLogConvert.convertForUpdate( sysAclModule.getId(),oldValue,newValue,LogEnum.ACL_MODULE_TABLE.getTableCode() ) );
        }

        //保存日志
        return sysAclModule;
    }


    /**
     * 新增
     * @param sysAclModuleDto
     * @return
     */
    @Override
    @Transactional
    public SysAclModule save(SysAclModuleDto sysAclModuleDto) {
        //父模块不存在则设置为0
        if(null==sysAclModuleDto.getParentId()){
            sysAclModuleDto.setParentId( SysConfig.aclModuleRootId );
        }
        //产生模块level 规则：父level.父id
        sysAclModuleDto.setModuleLevel(LevelUtil.getLevel( sysAclModuleDto.getParentId(),sysAclModuleDto.getModuleLevel()) );

        //检查相同父模块下是否名称重复
        SysAclModule parentModule=moduleRepository.findOne( sysAclModuleDto.getParentId() );
        if(null!=parentModule){
            if(null!=this.findByParentIdAndName( sysAclModuleDto.getParentId(),sysAclModuleDto.getName() )){
                log.error( "【新增模块异常】,msg={},code={}", ResultEnum.ACL_MODULE_IS_EXISTS.getMsg(),ResultEnum.ACL_MODULE_IS_EXISTS.getCode() );
                throw new ResultException( ResultEnum.ACL_MODULE_IS_EXISTS );
            }
        }

        SysAclModule aclModule= SysAclModuleConverter.convert( sysAclModuleDto );
        aclModule=moduleRepository.save( aclModule );
        if(null==aclModule){
            log.error( "【新增模块异常】,msg={},code={}", ResultEnum.ACL_MODULE_INSERT_ERROR.getMsg(),ResultEnum.ACL_MODULE_INSERT_ERROR.getCode() );
            throw new ResultException( ResultEnum.ACL_MODULE_INSERT_ERROR );
        }else{
            logService.save( SysLogConvert.convertForCD( aclModule.getId(), SysUtils.getJsonByObject( aclModule ), LogEnum.ACL_MODULE_TABLE.getTableCode(),LogEnum.OPERATOR_SAVE.getOperatorCode()) );
        }
        return aclModule;
    }

    /**
     * 查询父模块下重名
     * @param parentId
     * @param name
     * @return
     */
    @Override
    public SysAclModule findByParentIdAndName(Integer parentId, String name) {
        return moduleRepository.findByParentIdAndName( parentId,name );
    }

    /**
     * 查询父模块下的所有子模块
     * @param parentId
     * @return
     */
    @Override
    public List<SysAclModule> findByParentId(Integer parentId) {
        return moduleRepository.findByParentId( parentId );
    }

    /**
     * 查询所有模块
     * @return
     */
    @Override
    public List<SysAclModule> findAllAclModule() {

        List<Sort.Order> orders=new ArrayList< Sort.Order>();
        orders.add( new Sort.Order( Sort.Direction. ASC, "sort"));
        orders.add( new Sort.Order( Sort.Direction. ASC, "name"));

        Sort sort=new Sort(orders );
        return moduleRepository.findAll( sort );
    }

    @Override
    public SysAclModule findOne(Integer id) {
        return moduleRepository.findOne( id );
    }
}
