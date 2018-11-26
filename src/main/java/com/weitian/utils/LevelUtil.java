package com.weitian.utils;


import com.weitian.config.SysConfig;
import org.springframework.util.StringUtils;

/**
 *
 * 生成level
 * 自动生成部门level
 * 规则：
 * root的level值为：0
 * 次级节点为root+"."+parentId
 *
 * Created by Administrator on 2018/11/26.
 */
public class LevelUtil {
    public static String getLevel(Integer parentId,String parentLevel){
        if(StringUtils.isEmpty(parentLevel)){
            return SysConfig.deptRootLevel;
        }else{
            return parentLevel+SysConfig.separator+parentId;
        }
    }
}
