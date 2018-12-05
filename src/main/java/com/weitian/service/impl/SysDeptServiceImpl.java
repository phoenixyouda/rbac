package com.weitian.service.impl;

import com.weitian.config.SysConfig;
import com.weitian.convert.SysDeptConverter;
import com.weitian.convert.SysLogConvert;
import com.weitian.dto.SysDeptDto;
import com.weitian.entity.SysDept;
import com.weitian.entity.SysUser;
import com.weitian.enums.LogEnum;
import com.weitian.enums.ResultEnum;
import com.weitian.exception.ResultException;
import com.weitian.repository.SysDeptRepository;
import com.weitian.service.SysDeptService;
import com.weitian.service.SysLogService;
import com.weitian.utils.LevelUtil;
import com.weitian.utils.SysUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
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
     * 删除
     * @param id
     * @return
     */
    @Override
    @Transactional
    public void delete(Integer id) {

        //查询部门是否存在
        SysDept sysDept=this.findOne( id );
        if(null==sysDept){
            log.error( "【删除部门失败】,msg={},code={}",ResultEnum.DEPARTMENT_NOT_EXISTS.getMsg(),ResultEnum.DEPARTMENT_NOT_EXISTS.getCode() );
            throw new ResultException( ResultEnum.DEPARTMENT_NOT_EXISTS );
        }
        //查询是否包含子部门
        List<SysDept> sysDeptList=deptRepository.findByParentId( id );
        if(sysDeptList.size()>0){
            log.error( "【删除部门失败】,msg={},code={}",ResultEnum.DEPARTMENT_CONTAINS_CHILDDEPTS.getMsg(),ResultEnum.DEPARTMENT_CONTAINS_CHILDDEPTS.getCode());
            throw new ResultException( ResultEnum.DEPARTMENT_CONTAINS_CHILDDEPTS );
        }
        //部门中是否有人员存在
        List<SysUser> userList=sysDept.getSysUserList();
        if(userList.size()>0){
            log.error( "【删除部门失败】,msg={},code={}",ResultEnum.DEPARTMENT_CONTAINS_USERS.getMsg(),ResultEnum.DEPARTMENT_CONTAINS_USERS.getCode());
            throw new ResultException( ResultEnum.DEPARTMENT_CONTAINS_USERS );
        }
        //无人，可删
        deptRepository.delete( sysDept );

        logService.save( SysLogConvert.convertForCD( sysDept.getId(),SysUtils.getJsonByObject(sysDept ),LogEnum.DEPARTMENT_TABLE.getTableCode(),LogEnum.OPERATOR_DELETE.getOperatorCode() ) );

    }

    /**
     * 更新
     * @param deptDto
     * @return
     */
    @Override
    @Transactional
    public SysDept update(SysDeptDto deptDto) {

        SysDept oldDept=this.findOne( deptDto.getId() );


        String oldValue=SysUtils.getJsonByObject( oldDept );



        //查询部门名称是否重复
        SysDept dept=deptRepository.findByParentIdAndName( deptDto.getParentId(),deptDto.getName() );
        if(null!=dept){
            log.error( "【部门更新异常】,message={},code={}",ResultEnum.DEPARMENTNAME_IS_EXISTS.getMsg(),ResultEnum.DEPARMENTNAME_IS_EXISTS.getCode() );
            throw new ResultException( ResultEnum.DEPARMENTNAME_IS_EXISTS );
        }
        SysDept newDept=SysDeptConverter.convert( deptDto );
        String newValue=SysUtils.getJsonByObject( newDept );

        SysDept updateDept=deptRepository.save( newDept );

        if(null==updateDept){
            log.error( "【部门更新异常】,message={},code={}",ResultEnum.DEPARTMENT_UPDATE_ERROR.getMsg(),ResultEnum.DEPARTMENT_UPDATE_ERROR.getCode() );
            throw new ResultException( ResultEnum.DEPARTMENT_UPDATE_ERROR );
        }else{
            //更新日志
            logService.save( SysLogConvert.convertForUpdate( updateDept.getId(),oldValue,newValue,LogEnum.DEPARTMENT_TABLE.getTableCode() ));
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

        SysDept sysDept= SysDeptConverter.convert( deptDto );
        //保存

        sysDept=deptRepository.save( sysDept);
        if(null==sysDept){
            log.error( "【新增部门异常】,msg={},code={} ",ResultEnum.DEPARTMENT_INSERT_ERROR.getMsg(),ResultEnum.DEPARTMENT_INSERT_ERROR.getCode());
            throw new ResultException( ResultEnum.DEPARTMENT_INSERT_ERROR );
        }else{
            /*同步更新日志*/

            logService.save( SysLogConvert.convertForCD( sysDept.getId(), SysUtils.getJsonByObject(sysDept),LogEnum.DEPARTMENT_TABLE.getTableCode(),LogEnum.OPERATOR_SAVE.getOperatorCode() ) );
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
