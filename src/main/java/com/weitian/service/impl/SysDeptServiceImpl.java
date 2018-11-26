package com.weitian.service.impl;

import com.weitian.dto.SysDeptDto;
import com.weitian.entity.SysDept;
import com.weitian.enums.ResultEnum;
import com.weitian.exception.ResultException;
import com.weitian.repository.SysDeptRepository;
import com.weitian.service.SysDeptService;
import com.weitian.utils.LevelUtil;
import com.weitian.utils.SysDept2SysDeptDtoConverter;
import com.weitian.utils.SysDeptDto2SysDeptConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2018/11/22.
 */
@Service
@Slf4j
public class SysDeptServiceImpl implements SysDeptService {
    @Autowired
    private SysDeptRepository deptRepository;
    @Override
    public SysDept findOne(Integer id) {
        return deptRepository.findOne( id );
    }

    @Override
    public String getDeptLevelByParentId(Integer parentId) {
        return null;
    }

    /**
     * 查询所有部门
     * @return
     */
    @Override
    public List<SysDept> findAllDept() {
        return deptRepository.findAll();
    }




    /**
     * 新增部门
     * @param deptDto
     * @return
     */
    @Override
    public SysDept save(SysDeptDto deptDto) {

        deptDto.setLevel( LevelUtil.getLevel( deptDto.getParentId(),deptDto.getLevel() ) );

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
