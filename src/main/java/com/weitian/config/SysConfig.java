package com.weitian.config;

import lombok.Data;
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

    public static String deptRoot;

    public static String separator;

    @Value( "${deptRoot}" )
    public void setDeptRoot(String deptRoot){
        SysConfig.deptRoot=deptRoot;
    }
    @Value( "${separator}" )
    public void setSeparator(String separator) {
        SysConfig.separator = separator;
    }
}
