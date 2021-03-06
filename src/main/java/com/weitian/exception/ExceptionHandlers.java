package com.weitian.exception;

import com.weitian.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

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

        ModelAndView mv=null;
        Map<String,Object> errorMap=new HashMap<>(  );

        if(this.isJson( request )){
//            if(ex instanceof ResultException){
            log.error("request json error info:{},url:{},error code:{}", ex.getMessage(),request.getRequestURL().toString(),((ResultException) ex).getCode() );
            errorMap.put("exception",ResultVO.fail(ex.getMessage()));
            mv=new ModelAndView( "jsonView",errorMap );

        }else{

            log.error("request page error info:{},url:{}",ex.getMessage(),request.getRequestURL().toString());
            ex.printStackTrace();
            errorMap.put("exception",ResultVO.fail( ex.getMessage()));

            mv=new ModelAndView( "pageView",errorMap );
        }
       return mv;
    }
}
