package com.weitian.controller;

import com.weitian.convert.SysLogConvert;
import com.weitian.entity.SysUser;
import com.weitian.enums.LogEnum;
import com.weitian.enums.ResultEnum;
import com.weitian.service.SysLogService;
import com.weitian.service.SysUserService;
import com.weitian.utils.SysUtils;
import com.weitian.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by Administrator on 2018/12/6.
 */
@Controller
@RequestMapping("/sys/admin")
public class AdminController {
    @Autowired
    private SysUserService userService;
    @Autowired
    private SysLogService logService;

    @RequestMapping("/index")
    public String index(){
        return "index";
    }
    @RequestMapping("/noAuth")
    public String noAuth(){
        return "noAuth";
    }

    @RequestMapping("/showLogin")
    public String showLogin(){
        return "login";
    }

    @RequestMapping("/login")
    @ResponseBody
    public ResultVO login(@RequestParam("username") String username, @RequestParam("password") String password, HttpServletRequest request){
        if(StringUtils.isEmpty( username ) || StringUtils.isEmpty( password )){
            return ResultVO.fail( ResultEnum.USERNAME_OR_PASSWORD_NOTNULL.getMsg() );
        }
        SysUser sysUser=userService.login( username,password );
        if(sysUser==null){
            return ResultVO.fail( ResultEnum.USERNAME_OR_PASSWORD_IS_ERROR.getMsg() );
        }

        //登录成功
        HttpSession session=request.getSession();
        session.setAttribute( "loginUser",sysUser );
        logService.save( SysLogConvert.convertForCD( sysUser.getId(), SysUtils.getJsonByObject( sysUser ), LogEnum.USER_TABLE.getTableCode(),LogEnum.OPERATOR_LOGIN.getOperatorCode() ) );
        return ResultVO.success( ResultEnum.SUCCESS.getMsg() );
    }
    @RequestMapping("/logout")
    @ResponseBody
    public ResultVO logout(HttpSession session){

        SysUser sysUser=(SysUser)session.getAttribute( "loginUser" );
        if(null!=sysUser){
            logService.save( SysLogConvert.convertForCD( sysUser.getId(), SysUtils.getJsonByObject( sysUser ), LogEnum.USER_TABLE.getTableCode(),LogEnum.OPERATOR_LOGOUT.getOperatorCode() ) );
        }
        session.invalidate();

        return ResultVO.success( ResultEnum.SUCCESS.getMsg() );
    }
}
