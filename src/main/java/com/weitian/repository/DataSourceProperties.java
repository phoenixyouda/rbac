package com.weitian.repository;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;

import lombok.Getter;
import lombok.Setter;


@ConfigurationProperties(prefix = "spring.datasource")
@Component
@Getter
@Setter
@Configuration
public class DataSourceProperties {
	private String url;
	private String username;
	private String password;
	private String driverClassName;
	private int maxActive;
	private int initialSize;
	private int maxWait;
	private int minIdle;
	private int timeBetweenEvictionRunsMillis;
	private int minEvictableIdleTimeMillis;
	private String validationQuery;
	private boolean testWhileIdle;
	private boolean testOnBorrow;
	private boolean testOnReturn;
	private int maxOpenPreparedStatements;
	private boolean poolPreparedStatements;
	private String filters;
	private String connectionProperties;
	private boolean removeAbandoned;
	private int removeAbandonedTimeout;
	
	@Bean
	@Primary  // 在同样的DataSource中，首先使用被标注的DataSource
	public DataSource getDataSource(){
		DruidDataSource dataSource=new DruidDataSource();
		dataSource.setUrl(url);
		dataSource.setUsername(username);
		dataSource.setPassword(password);
		dataSource.setDriverClassName(driverClassName);
		dataSource.setMaxActive(maxActive);
		dataSource.setInitialSize(initialSize);
		dataSource.setMaxWait(maxWait);
		dataSource.setMinIdle(minIdle);
		dataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
		dataSource.setValidationQuery(validationQuery);
		dataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
		dataSource.setTestWhileIdle(testWhileIdle);
		dataSource.setTestOnBorrow(testOnBorrow);
		dataSource.setTestOnReturn(testOnReturn);
		dataSource.setMaxOpenPreparedStatements(maxOpenPreparedStatements);
		dataSource.setPoolPreparedStatements(poolPreparedStatements);
		try {
			dataSource.setFilters(filters);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dataSource.setConnectionProperties(connectionProperties);
		dataSource.setRemoveAbandoned(removeAbandoned);
		dataSource.setRemoveAbandonedTimeout(removeAbandonedTimeout);
		return dataSource;
		
	}
	
	@Bean
	public FilterRegistrationBean getFilterRegistrationBean(){
		FilterRegistrationBean filterRegistrationBean=new FilterRegistrationBean();
		filterRegistrationBean.addUrlPatterns("/*");
		filterRegistrationBean.setFilter(new WebStatFilter());
		return filterRegistrationBean;
	}
	@Bean
	public ServletRegistrationBean getServletRegistrationBean(){
		ServletRegistrationBean servletRegistrationBean=new ServletRegistrationBean();
		servletRegistrationBean.addUrlMappings("/druid/*");
		servletRegistrationBean.setServlet(new StatViewServlet());
		
		return servletRegistrationBean;
	}
	
	
}
