package com.weitian.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2018/11/26.
 */
@Component
@ConfigurationProperties(prefix = "spring.sysConfig")
@PropertySource( "classpath:SysConfig.yml" )
public class SysConfig {

    public static String deptRootLevel;

    public static Integer deptRootId;

    public static String aclModuleRootLevel;
    public static Integer aclModuleRootId;

    public static String separator;

    @Value( "${aclModuleRootLevel}" )
    public void setAclModuleRootLevel(String aclModuleRootLevel) {
        SysConfig.aclModuleRootLevel=aclModuleRootLevel;
    }

    @Value("${aclModuleRootId}")
    public void setAclModuleRootId(Integer aclModuleRootId) {
        SysConfig.aclModuleRootId=aclModuleRootId;
    }

    @Value("${deptRootId}")
    public void setDeptRootId(Integer deptRootId) {
        SysConfig.deptRootId = deptRootId;
    }

    @Value( "${deptRootLevel}" )
    public void setDeptRootLevel(String deptRoot){
        SysConfig.deptRootLevel=deptRoot;
    }
    @Value( "${separator}" )
    public void setSeparator(String separator) {
        SysConfig.separator = separator;
    }
}
