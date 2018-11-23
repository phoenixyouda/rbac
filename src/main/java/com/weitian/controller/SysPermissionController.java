package com.weitian.controller;

import com.weitian.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Administrator on 2018/11/23.
 */
@Controller

public class SysPermissionController {
    @Autowired
    private SysUserService userService;
    @RequestMapping("/test")
    @ResponseBody
    public String test(){
        userService.findById( null );
        return "123";
    }
}
