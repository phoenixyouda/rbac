package com.weitian.filter;

import com.sun.javafx.scene.control.skin.VirtualFlow;
import com.weitian.entity.SysAcl;
import com.weitian.entity.SysRole;
import com.weitian.entity.SysUser;

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
public class AclControlFilter implements Filter {
    /**
     * 封装，不需要过滤的list列表
     */
    public static List<String> exclusionUrls = new ArrayList<String>();

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

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request=(HttpServletRequest)servletRequest;
        HttpServletResponse response=(HttpServletResponse)servletResponse;
        HttpSession session=request.getSession();


        String url = request.getRequestURI().substring(request.getContextPath().length());

        if(url.contains( ".css" ) || url.contains( ".js" ) || url.contains( ".jpg" ) || url.contains( ".png" ) || url.contains( ".gif" )||url.equals( ".ico" ) ){
            chain.doFilter(request, response);
            return;
        }
        if(isExclude(url)){
            chain.doFilter( request,response );
            return;
        }

        //session中获取用户
        SysUser sysUser=(SysUser)session.getAttribute( "loginUser" );
        //admin用户直接放行
        if(sysUser.getUsername().equals( "admin" )){
            chain.doFilter( request,response );
            return;
        }
        //其他用户根据权限点控制访问
        List<SysRole> roleList=sysUser.getSysRoleList();
        List<String> aclUrls=new ArrayList<>(  );
        for(SysRole role:roleList){
            for(SysAcl sysAcl:role.getSysAclList()){
                aclUrls.add( sysAcl.getUrl() );
            }
        }

        //跳转无权限访问页面

        if(aclUrls.contains( url )){
            chain.doFilter( request,response );
            return;
        }else{
            request.getRequestDispatcher( request.getContextPath()+"/sys/admin/noAuth").forward( request,response   );
            return;
        }
    }

    @Override
    public void destroy() {

    }
}
