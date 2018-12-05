package com.weitian.controller;

import antlr.StringUtils;
import com.weitian.dto.SysUserDto;
import com.weitian.entity.SysDept;
import com.weitian.entity.SysUser;
import com.weitian.enums.ResultEnum;
import com.weitian.exception.ResultException;
import com.weitian.form.UserForm;
import com.weitian.service.SysUserService;
import com.weitian.convert.SysUserConverter;
import com.weitian.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/11/29.
 */
@Controller
@RequestMapping("/sys/user")
public class SysUserController {
    @Autowired
    private SysUserService userService;


    @RequestMapping("/queryById")
    @ResponseBody
    public ResultVO queryById(@RequestParam("id")Integer id){
        if(null==id){
            return ResultVO.fail( ResultEnum.PARAM_IS_ERROR.getMsg() );
        }
        SysUser sysUser=userService.findById( id );
        return ResultVO.success( ResultEnum.SUCCESS.getMsg(),SysUserConverter.convert2UserDto( sysUser ) );
    }

    /**
     * 删除用户
     * @param id
     * @return
     */
    @RequestMapping("/delete")
    @ResponseBody
    public ResultVO delete(@RequestParam("id")Integer id){
        if(null==id){
            return ResultVO.fail( ResultEnum.PARAM_IS_ERROR.getMsg() );
        }

        try {
            userService.delete( id );
            return ResultVO.success( ResultEnum.SUCCESS.getMsg() );
        }catch(Exception ex){
            return ResultVO.fail( ResultEnum.USER_DELETE_ERROR.getMsg() );
        }
    }

    @PostMapping("/save")
    @ResponseBody
    public ResultVO save(@Valid UserForm userForm, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return ResultVO.fail( bindingResult.getFieldError().getDefaultMessage() );
        }
        SysUserDto userDto=SysUserConverter.convert( userForm );
        try {
            if(null==userDto.getId()) {
                return ResultVO.success( ResultEnum.SUCCESS.getMsg(), userService.save( userDto ) );
            }else{
                return ResultVO.success( ResultEnum.SUCCESS.getMsg(), userService.update( userDto ) );
            }
        } catch (Exception e) {
            return ResultVO.fail( ResultEnum.USER_INSERT_ERROR.getMsg() );
        }
    }

    /**
     * 分页显示部门下人员
     * @param currPage
     * @param pageSize
     * @param deparmentId
     * @return
     */
    @PostMapping("/userList")
    @ResponseBody
    public ResultVO userList(@RequestParam(name="currPage",defaultValue = "1")Integer currPage, @RequestParam(name="pageSize",defaultValue = "10") Integer pageSize,@RequestParam(name="departmentId",defaultValue = "1") Integer deparmentId){

        Page<SysUser> page=userService.findAll( currPage-1,pageSize,deparmentId );

        Map<String,Object> map=new HashMap<String,Object>(  );
        map.put( "list", SysUserConverter.convert2UserDto(page.getContent()) );
        map.put( "currPage",currPage );
        map.put( "pageSize",pageSize );
        Integer from=(currPage-1)*pageSize+1;
        Integer to=from+page.getContent().size()-1;
        map.put("from",from);
        map.put( "to",to );
        map.put( "totalCounts",page.getTotalElements() );
        map.put( "totalPages",page.getTotalPages() );

        return ResultVO.success( ResultEnum.SUCCESS.getMsg(), map);
    }

}
