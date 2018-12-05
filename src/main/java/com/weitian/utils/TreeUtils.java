package com.weitian.utils;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.weitian.config.SysConfig;
import com.weitian.convert.SysAclModuleConverter;
import com.weitian.convert.SysDeptConverter;
import com.weitian.dto.SysAclModuleDto;
import com.weitian.dto.SysDeptDto;
import com.weitian.entity.SysAclModule;
import com.weitian.entity.SysDept;

import java.util.List;

public class TreeUtils {


    /**
     * 查询所有权限模块
     * @param moduleList
     * @return
     */
    public static List<SysAclModuleDto> findAllModule(List<SysAclModule> moduleList){
        //Multimap相当于Map<String,List>,以层级为key，deptlist为value
        Multimap<String,SysAclModuleDto>  moduleLevelMap= ArrayListMultimap.create();
        List<SysAclModuleDto> sysAclModuleDtoList = Lists.newArrayList();

        for(SysAclModule sysAclModule:moduleList){
            //获取所有层级与对应该层级的部门
            moduleLevelMap.put( sysAclModule.getModuleLevel(), SysAclModuleConverter.convert(sysAclModule));
            //确定部门根节点
            if(SysConfig.aclModuleRootLevel.equals(sysAclModule.getModuleLevel())){
                SysAclModuleDto sysAclModuleDto= SysAclModuleConverter.convert(sysAclModule);
                //设置第一级下节点默认打开状态
                sysAclModuleDto.setOpen( true );
                sysAclModuleDtoList.add( sysAclModuleDto );
            }
        }
        //产生部门树

        aclModuleTree(sysAclModuleDtoList,SysConfig.aclModuleRootLevel,moduleLevelMap);
        return sysAclModuleDtoList;
    }

    public static void aclModuleTree(List<SysAclModuleDto> sysAclModuleDtoList,String level,Multimap<String,SysAclModuleDto> levelMap){


        for(SysAclModuleDto aclModuleDto:sysAclModuleDtoList){
            //当前部门层级
            String currentLevel=level+SysConfig.separator+aclModuleDto.getId();
            //获取该层级下所有子部门
            List<SysAclModuleDto> currentLevelList=(List<SysAclModuleDto>)levelMap.get( currentLevel );
            //添加到本部门的children
            aclModuleDto.setChildren(currentLevelList);
            //递归调用
            aclModuleTree(currentLevelList,currentLevel,  levelMap);
        }
    }


    /**
     * 查询所有部门
     * @param deptList
     * @return
     */
    public static List<SysDeptDto> findAll(List<SysDept> deptList){

        //Multimap相当于Map<String,List>,以层级为key，deptlist为value
        Multimap<String,SysDeptDto>  deptLevelMap= ArrayListMultimap.create();
        List<SysDeptDto> sysDeptDtoList = Lists.newArrayList();

        for(SysDept sysDept:deptList){
            //获取所有层级与对应该层级的部门
            deptLevelMap.put( sysDept.getDeptLevel(), SysDeptConverter.convert(sysDept));
            //确定部门根节点
            if(SysConfig.deptRootLevel.equals(sysDept.getDeptLevel())){
                SysDeptDto sysDeptDto= SysDeptConverter.convert(sysDept);
                //设置第一级下节点默认打开状态
                sysDeptDto.setOpen( true );
                sysDeptDtoList.add( sysDeptDto );
            }
        }
        //产生部门树

        deptTree(sysDeptDtoList,SysConfig.deptRootLevel,deptLevelMap);
        return sysDeptDtoList;
    }

    //部门树格式
    /*[
        {
            id: 1,
                    name: "伟天化工",
                level: "0",
                parentId: 0,
                sort: 0,
                remark: "",
                operator: "user",
                operatorTime: 1543218901000,
                operatorIP: "127.0.0.1",
                sysUserList: [ ],
            children: [
            {
                id: 2,
                        name: "信息部",
                    level: "0.1",
                    parentId: 1,
                    sort: 0,
                    remark: "",
                    operator: "user",
                    operatorTime: 1543218917000,
                    operatorIP: "127.0.0.1",
                    sysUserList: [ ],
                children: [
                {
                    id: 4,
                            name: "软件处",
                        level: "0.1.2",
                        parentId: 2,
                        sort: 0,
                        remark: "",
                        operator: "user",
                        operatorTime: 1543218940000,
                        operatorIP: "127.0.0.1",
                        sysUserList: [ ],
                    children: [ ],
                    open: false
                }],
                open: false
            }],
            open: false
        }
]*/

    public static void deptTree(List<SysDeptDto> sysDeptDtoList,String level,Multimap<String,SysDeptDto> levelMap){


        for(SysDeptDto sysDept:sysDeptDtoList){
            //当前部门层级
            String currentLevel=level+SysConfig.separator+sysDept.getId();
            //获取该层级下所有子部门
            List<SysDeptDto> currentLevelDeptList=(List<SysDeptDto>)levelMap.get( currentLevel );
            //添加到本部门的children
            sysDept.setChildren(currentLevelDeptList);
            //递归调用
            deptTree(currentLevelDeptList,currentLevel,  levelMap);
        }
    }

}
