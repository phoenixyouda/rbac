package com.weitian.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Administrator on 2018/12/6.
 */
@Controller
@RequestMapping("/sys/admin")
public class AdminController {

    @RequestMapping("/index")
    public String index(){
        return "index";
    }
}
