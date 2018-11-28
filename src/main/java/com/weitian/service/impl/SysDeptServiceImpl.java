package com.weitian.service.impl;

import com.google.j2objc.annotations.AutoreleasePool;
import com.weitian.config.SysConfig;
import com.weitian.dto.SysDeptDto;
import com.weitian.entity.SysDept;
import com.weitian.enums.LogEnum;
import com.weitian.enums.ResultEnum;
import com.weitian.exception.ResultException;
import com.weitian.repository.SysDeptRepository;
import com.weitian.service.SysDeptService;
import com.weitian.service.SysLogService;
import com.weitian.utils.LevelUtil;
import com.weitian.utils.SysDept2SysLogConvert;
import com.weitian.utils.SysDeptDto2SysDeptConverter;
import com.weitian.utils.SysUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Administrator on 2018/11/22.
 */
@Service
@Slf4j
public class SysDeptServiceImpl implements SysDeptService {
    @Autowired
    private SysDeptRepository deptRepository;
    @Autowired
    private SysLogService logService;
    /**
     * 根据主键查询部门信息
     * @param id
     * @return
     */
    @Override
    public SysDept findOne(Integer id) {
        return deptRepository.findOne( id );
    }


    /**
     *
     * @param deptDto
     * @return
     */
    @Override
    @Transactional
    public SysDept update(SysDeptDto deptDto) {

        SysDept oldDept=this.findOne( deptDto.getId() );

        SysDept newDept=new SysDept();
        BeanUtils.copyProperties( oldDept,newDept );
        newDept.setName( deptDto.getName() );
        newDept.setOperator( SysUtils.getSessionUserName() );
        newDept.setOperatorIP( SysUtils.getSessionUserIp() );
        SysDept updateDept=deptRepository.save( newDept );
        if(null==updateDept){
            log.error( "【部门更新异常】,message={},code={}",ResultEnum.DEPARTMENT_UPDATE_ERROR.getMsg(),ResultEnum.DEPARTMENT_UPDATE_ERROR.getCode() );
            throw new ResultException( ResultEnum.DEPARTMENT_UPDATE_ERROR );
        }else{
            //更新日志
            logService.save( SysDept2SysLogConvert.convert( oldDept,newDept,LogEnum.OPERATOR_UPDATE.getOperatorCode() ) );
        }
        return updateDept;
    }

    /**
     * 查询所有部门
     * @return
     */
    @Override
    public List<SysDept> findAllDept() {
        Sort sort=new Sort( Sort.Direction.ASC,"sort");
        return deptRepository.findAll(sort);
    }




    /**
     * 新增部门
     * @param deptDto
     * @return
     */
    @Override
    @Transactional
    public SysDept save(SysDeptDto deptDto) {

        //父部门不存在则设置为0
        if(null==deptDto.getParentId()){
            deptDto.setParentId( SysConfig.deptRootId);
        }

        //生成部门level 规则：父部门level.父部门id
        deptDto.setDeptLevel( LevelUtil.getLevel( deptDto.getParentId(),deptDto.getDeptLevel() ) );

        SysDept deptParent=this.findOne( deptDto.getParentId() );

        if(null!=deptParent){
            // 检查部门名称是否重复
            if(null!=this.findByParentIdAndName( deptDto.getParentId(),deptDto.getName() )){
                log.error( "【部门异常】,msg={},code={}",ResultEnum.DEPARMENTNAME_IS_EXISTS.getMsg(),ResultEnum.DEPARMENTNAME_IS_EXISTS.getCode() );
                throw new ResultException( ResultEnum.DEPARMENTNAME_IS_EXISTS );
            }
        }

        SysDept sysDept=SysDeptDto2SysDeptConverter.convert( deptDto );
        //保存
        sysDept.setOperator( "user" );  //TODO
        sysDept.setOperatorIP( "127.0.0.1" );//TODO
        sysDept=deptRepository.save( sysDept);
        if(null==sysDept){
            log.error( "【部门异常】,msg={},code={} ",ResultEnum.DEPARTMENT_INSERT_ERROR.getMsg(),ResultEnum.DEPARTMENT_INSERT_ERROR.getCode());
            throw new ResultException( ResultEnum.DEPARTMENT_INSERT_ERROR );
        }else{
            /*同步更新日志*/
            logService.save( SysDept2SysLogConvert.convert( sysDept,null, LogEnum.OPERATOR_SAVE.getOperatorCode() ) );
        }
        return sysDept;
    }

    /**
     * 查询父部门下同名的部门
     * @param parentId
     * @param name
     * @return
     */
    @Override
    public SysDept findByParentIdAndName(Integer parentId, String name) {
        return deptRepository.findByParentIdAndName( parentId,name );

    }
}
