package com.weitian.filter;

import lombok.Data;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/12/12.
 */

public class SessionFilter implements Filter {

    /**
     * 封装，不需要过滤的list列表
     */
    public static List<String> exclusionUrls = new ArrayList<String>();


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

        String url = httpRequest.getRequestURI().substring( httpRequest.getRequestURI().lastIndexOf( "/" ) );


        if(url.contains( ".css" ) || url.contains( ".js" ) || url.contains( ".jpg" ) || url.contains( ".png" ) || url.contains( ".gif" )||url.equals( ".ico" ) ){
            chain.doFilter(httpRequest, httpResponse);
            return;
        }

        if (isExclude(url)){
            chain.doFilter(httpRequest, httpResponse);
            return;
        } else {
            HttpSession session = httpRequest.getSession();
            if (session.getAttribute("loginUser") != null){
                // session存在
                chain.doFilter(httpRequest, httpResponse);
                return;
            } else {
                // session不存在 准备跳转失败
                httpResponse.sendRedirect(httpRequest.getContextPath()+"/sys/admin/showLogin");
                return;
            }
        }

    }

    @Override
    public void destroy() {

    }

    /**
     * 是否需要过滤
     * @param url
     * @return
     */
    private boolean isExclude(String url) {
        for (String exclusionUrl : exclusionUrls) {
            if(exclusionUrl.equals( url )){
                return true;
            }
        }
        return false;
    }
}
