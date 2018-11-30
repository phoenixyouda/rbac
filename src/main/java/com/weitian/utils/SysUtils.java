package com.weitian.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.weitian.entity.SysUser;
import com.weitian.enums.CodeEnum;

/**
 * Created by Administrator on 2018/11/28.
 */
public class SysUtils {

    public static String getSessionUserName(){
        //获取session
        //从session中获取登录用户姓名
        return "user";
    }
    public static String getSessionUserIp(){
        //获取session
        //从session中获取登录用户IP
        return "127.0.0.1";
    }

    //状态码转换为描述
    public static <T extends CodeEnum> T getByCode(Integer code, Class<T> enumCls){
        for(T each:enumCls.getEnumConstants()){
            if(code.equals( each.getCode() )){
                return each;
            }
        }
        return null;
    }

    //对象转为json
    public static String getJsonByObject(Object obj){
        Gson gson= new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        return gson.toJson( obj );
    }
}
