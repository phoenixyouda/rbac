package com.weitian.config;

import com.weitian.filter.AclControlFilter;
import com.weitian.filter.SessionFilter;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

/**
 * Created by Administrator on 2018/12/12.
 */
@Configuration
public class FilterConfig {
    @Bean
    public FilterRegistrationBean sessionFilterRegistration(@Qualifier("sessionFilter") SessionFilter sessionFilter) {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(sessionFilter);
        registration.addUrlPatterns("/*");
        //多个过滤器时设置执行顺序，优先级从小到大依次优先
        registration.setOrder(0 );
        return registration;
    }
    @Bean
    public FilterRegistrationBean aclFilterRegistration(@Qualifier("aclControlFilter") AclControlFilter aclControlFilter) {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(aclControlFilter);
        registration.addUrlPatterns("/sys/*");
        //多个过滤器时设置执行顺序，优先级从小到大依次优先
        registration.setOrder(1);
        return registration;
    }
    @Bean(name = "sessionFilter")
    public SessionFilter sessionFilter() {
        SessionFilter sessionFilter=new SessionFilter();
        SessionFilter.exclusionUrls.add( "/login" );
        SessionFilter.exclusionUrls.add( "/showLogin" );
        SessionFilter.exclusionUrls.add( "/logout" );
        return sessionFilter;
    }
    @Bean(name = "aclControlFilter")
    public AclControlFilter aclControlFilter() {
        AclControlFilter aclFilter=new AclControlFilter();
        aclFilter.exclusionUrls.add( "/sys/admin/login" );
        aclFilter.exclusionUrls.add( "/sys/admin/showLogin" );
        aclFilter.exclusionUrls.add( "/sys/admin/logout" );
        aclFilter.exclusionUrls.add( "/sys/admin/noAuth" );
        return aclFilter;
    }
}
