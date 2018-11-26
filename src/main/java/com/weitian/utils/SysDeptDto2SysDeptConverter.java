package com.weitian.utils;

import com.weitian.dto.SysDeptDto;
import com.weitian.entity.SysDept;
import org.springframework.beans.BeanUtils;

/**
 * Created by Administrator on 2018/11/26.
 */
public class SysDeptDto2SysDeptConverter {
    public static SysDept convert(SysDeptDto sysDeptDto){
        SysDept sysDept=new SysDept();
        BeanUtils.copyProperties( sysDeptDto,sysDept );
        return sysDept;
    }
}
