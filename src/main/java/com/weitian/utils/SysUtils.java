package com.weitian.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.weitian.entity.SysUser;
import com.weitian.enums.CodeEnum;
import com.weitian.enums.LogEnum;
import freemarker.template.SimpleDate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;
import java.util.logging.SimpleFormatter;

/**
 * Created by Administrator on 2018/11/28.
 */
public class SysUtils {

    public static String getSessionUserName() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        //获取session
        HttpSession session=request.getSession();
        String name="匿名";
        if(null!=session.getAttribute( "loginUser" )) {
            name = ((SysUser) session.getAttribute( "loginUser" )).getUsername();
        }
        //从session中获取登录用户姓名
        return name;
    }

    public static String getSessionUserIp() {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String ip = getIpAddr( request );
        return ip;
    }


    public static String getIpAddr(HttpServletRequest request) {
        String ipAddress = null;
        try {
            ipAddress = request.getHeader( "x-forwarded-for" );
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase( ipAddress )) {
                ipAddress = request.getHeader( "Proxy-Client-IP" );
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase( ipAddress )) {
                ipAddress = request.getHeader( "WL-Proxy-Client-IP" );
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase( ipAddress )) {
                ipAddress = request.getRemoteAddr();
                if (ipAddress.equals( "127.0.0.1" )) {
                    // 根据网卡取本机配置的IP
                    InetAddress inet = null;
                    try {
                        inet = InetAddress.getLocalHost();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    ipAddress = inet.getHostAddress();
                }
            }
            // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
            if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length()
                // = 15
                if (ipAddress.indexOf( "," ) > 0) {
                    ipAddress = ipAddress.substring( 0, ipAddress.indexOf( "," ) );
                }
            }
        } catch (Exception e) {
            ipAddress = "";
        }
        // ipAddress = this.getRequest().getRemoteAddr();

        return ipAddress;
    }

    //状态码转换为描述
    public static <T extends CodeEnum> T getByCode(Integer code, Class<T> enumCls) {
        for (T each : enumCls.getEnumConstants()) {
            if (code.equals( each.getCode() )) {
                return each;
            }
        }
        return null;
    }


    public static LogEnum getByTableCode(Integer code){
        for(LogEnum logEnum:LogEnum.class.getEnumConstants()){
            if(code.equals( logEnum.getTableCode() )){
                return logEnum;
            }
        }
        return null;
    }
    public static LogEnum getByOperatorCode(Integer code){
        for(LogEnum logEnum:LogEnum.class.getEnumConstants()){
            if(code.equals( logEnum.getOperatorCode() )){
                return logEnum;
            }
        }
        return null;
    }
    public static LogEnum getByStatusCode(Integer code){
        for(LogEnum logEnum:LogEnum.class.getEnumConstants()){
            if(code.equals( logEnum.getStatusCode() )){
                return logEnum;
            }
        }
        return null;
    }

    //对象转为json
    public static String getJsonByObject(Object obj) {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        return gson.toJson( obj );
    }

    //生成权限点code码(16位)
    public static String getCodeForAcl(){

        String code= UUID.randomUUID().toString().replace( "-","" );
        return code;
    }
}
