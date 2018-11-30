package com.weitian.convert;

import com.google.common.collect.Lists;
import com.weitian.dto.SysDeptDto;
import com.weitian.entity.SysDept;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Created by Administrator on 2018/11/26.
 */
public class SysDeptConverter {
    public static SysDeptDto convert(SysDept sysDept){
        SysDeptDto sysDeptDto=new SysDeptDto();
        BeanUtils.copyProperties( sysDept, sysDeptDto);
        return sysDeptDto;
    }

    public static List<SysDeptDto> convert(List<SysDept> sysDeptList){
        List<SysDeptDto> sysDeptDtoList= Lists.newArrayList();
        if(!CollectionUtils.isEmpty( sysDeptList )){
            for(SysDept sysDept:sysDeptList){
                sysDeptDtoList.add( convert(sysDept) );
            }

        }
        return sysDeptDtoList;
    }

    public static SysDept convert(SysDeptDto sysDeptDto){
        SysDept sysDept=new SysDept();
        BeanUtils.copyProperties( sysDeptDto,sysDept );
        return sysDept;
    }
}
