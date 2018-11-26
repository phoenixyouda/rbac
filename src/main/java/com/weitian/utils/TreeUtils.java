package com.weitian.utils;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.weitian.config.SysConfig;
import com.weitian.dto.SysDeptDto;
import com.weitian.entity.SysDept;

import java.util.List;

public class TreeUtils {

    public static List<SysDeptDto> findAll(List<SysDept> deptList){

        Multimap<String,SysDeptDto>  deptLevelMap= ArrayListMultimap.create();
        List<SysDeptDto> sysDeptDtoList = Lists.newArrayList();

        for(SysDept sysDept:deptList){
            deptLevelMap.put( sysDept.getLevel(),SysDept2SysDeptDtoConverter.convert(sysDept));
            if(SysConfig.deptRootLevel.equals(sysDept.getLevel())){
                sysDeptDtoList.add( SysDept2SysDeptDtoConverter.convert(sysDept) );
            }
        }
        deptTree(sysDeptDtoList,SysConfig.deptRootLevel,deptLevelMap);
        return sysDeptDtoList;
    }

    public static void deptTree(List<SysDeptDto> sysDeptDtoList,String level,Multimap<String,SysDeptDto> levelMap){


        for(SysDeptDto sysDept:sysDeptDtoList){
            String currentLevel=level+"."+sysDept.getId();

            List<SysDeptDto> currentLevelDeptList=(List<SysDeptDto>)levelMap.get( currentLevel );

            sysDept.setChildDeptList(currentLevelDeptList);

            deptTree(currentLevelDeptList,currentLevel,  levelMap);
        }
    }

}
