package com.weitian.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Administrator on 2018/11/22.
 */
@Component
@Slf4j
public class ExceptionHandlers implements HandlerExceptionResolver{

    //判断当前请求是json还是view
    private Boolean isJson(HttpServletRequest request){
        String header = request.getHeader("content-type");
        return header != null && header.contains("json");
    }
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

        //与前端

        if(this.isJson( request )){
            if(ex instanceof ResultException){
                log.error("request json error info:{0},url:{1},error code:{2}", ((ResultException) ex).getMsg(),request.getRequestURL().toString(),((ResultException) ex).getCode() );
            }else{

            }
        }else{

        }
        return null;
    }
}
