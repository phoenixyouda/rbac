package com.weitian.service.impl;

import com.weitian.dto.SysDeptDto;
import com.weitian.entity.SysDept;
import com.weitian.enums.ResultEnum;
import com.weitian.exception.ResultException;
import com.weitian.repository.SysDeptRepository;
import com.weitian.service.SysDeptService;
import com.weitian.utils.SysDeptDto2SysDeptConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public SysDept save(SysDeptDto deptDto) {
        SysDept deptParent=this.findOne( deptDto.getParentId() );
        // 检查部门名称是否重复
        if(null!=this.findByParentIdAndName( deptDto.getParentId(),deptDto.getName() )){
            log.error( "【部门异常】,msg={},code={}",ResultEnum.DEPARMENTNAME_IS_EXISTS.getMsg(),ResultEnum.DEPARMENTNAME_IS_EXISTS.getCode() );
            throw new ResultException( ResultEnum.DEPARMENTNAME_IS_EXISTS );
        }
        //保存
        SysDept sysDept=deptRepository.save( SysDeptDto2SysDeptConverter.convert( deptDto ) );

        return sysDept;
    }

    @Override
    public SysDept findByParentIdAndName(Integer parentId, String name) {
        return deptRepository.findByParentIdAndName( parentId,name );

    }
}
