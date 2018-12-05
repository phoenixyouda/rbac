package com.weitian.controller;

import com.alibaba.druid.util.StringUtils;
import com.weitian.convert.SysAclModuleConverter;
import com.weitian.dto.SysAclModuleDto;
import com.weitian.entity.SysAcl;
import com.weitian.entity.SysAclModule;
import com.weitian.enums.ResultEnum;
import com.weitian.enums.StatusEnum;
import com.weitian.form.ModuleForm;
import com.weitian.service.SysAclModuleService;
import com.weitian.utils.TreeUtils;
import com.weitian.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by Administrator on 2018/12/5.
 */
@Controller
@RequestMapping("/sys/aclModule")
public class SysAclModuleController {

    @Autowired
    private SysAclModuleService moduleService;
    @RequestMapping("/show")
    public String show(){
        return "/acl/acl";
    }

    @RequestMapping("/update")
    @ResponseBody
    public ResultVO update(@Valid ModuleForm moduleForm,BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return ResultVO.fail( bindingResult.getFieldError().getDefaultMessage() );
        }
        SysAclModule sysAclModule=moduleService.findOne( moduleForm.getId() );
        SysAclModuleDto sysAclModuleDto=SysAclModuleConverter.convert( sysAclModule );
        sysAclModuleDto.setName( moduleForm.getName() );
        try{
            sysAclModule=moduleService.update( sysAclModuleDto );
            return ResultVO.success( ResultEnum.SUCCESS.getMsg(),sysAclModule );
        }catch(Exception ex){
            return ResultVO.fail( ex.getMessage() );
        }
    }

    @RequestMapping("/delete")
    @ResponseBody
    public ResultVO delete(@RequestParam("id")Integer id){
        if(null==id){
            return ResultVO.fail( ResultEnum.PARAM_IS_ERROR.getMsg() );
        }

        try{
            moduleService.delete( id );
            return ResultVO.success( ResultEnum.SUCCESS.getMsg() );
        }catch(Exception ex){
            return ResultVO.fail( ex.getMessage() );
        }
    }


    @RequestMapping("/save")
    @ResponseBody
    public ResultVO save(@Valid ModuleForm moduleForm, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return ResultVO.fail( ResultEnum.PARAM_IS_ERROR.getMsg() );
        }
        SysAclModuleDto aclModuleDto= SysAclModuleConverter.convert( moduleForm );
        try {
            SysAclModule aclModule = moduleService.save( aclModuleDto );
            return ResultVO.success( ResultEnum.SUCCESS.getMsg(),aclModule );
        }catch(Exception ex){
            return ResultVO.fail(ex.getMessage());
        }

    }

    /**
     * 显示模块树
     * @return
     */
    @RequestMapping("/tree")
    @ResponseBody
    public ResultVO tree(){
        List<SysAclModule> aclModuleList=moduleService.findAllAclModule();
        List<SysAclModuleDto> aclModuleDtoList= TreeUtils.findAllModule( aclModuleList );

        return ResultVO.success( ResultEnum.SUCCESS.getMsg(),aclModuleDtoList );
    }


}
