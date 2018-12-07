package com.weitian.controller;

import com.weitian.convert.SysAclConverter;
import com.weitian.dto.SysAclDto;
import com.weitian.entity.SysAcl;
import com.weitian.enums.ResultEnum;
import com.weitian.form.AclForm;
import com.weitian.service.SysAclService;
import com.weitian.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/12/6.
 */
@Controller
@RequestMapping("/sys/acl")
public class SysAclController {
    @Autowired
    private SysAclService aclService;

    @RequestMapping("/save")
    @ResponseBody
    public ResultVO save(@Valid AclForm aclForm, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return ResultVO.fail( bindingResult.getFieldError().getDefaultMessage() );
        }
        try {
            SysAclDto sysAclDto=SysAclConverter.convert( aclForm );
            if(null==sysAclDto.getId()) {
                return ResultVO.success( ResultEnum.SUCCESS.getMsg(), aclService.save(sysAclDto) );
            }else{
                return ResultVO.success( ResultEnum.SUCCESS.getMsg(), aclService.update(sysAclDto) );
            }
        }catch(Exception ex){
            return ResultVO.fail( ex.getMessage() );
        }
    }


    /**
     * 查询所有权限点
     * @param currPage
     * @param pageSize
     * @param moduleId
     * @return
     */
    @RequestMapping("/aclList")
    @ResponseBody
    public ResultVO aclList(@RequestParam(name="currPage",defaultValue = "1") Integer currPage, @RequestParam(name="pageSize",defaultValue = "10") Integer pageSize, @RequestParam(name="moduleId",defaultValue = "1") Integer moduleId){
        try{
            Page<SysAcl> aclPage=aclService.findByModuleId( currPage-1,pageSize,moduleId );
            Map<String,Object> map=new HashMap<String,Object>(  );
            map.put( "list", SysAclConverter.convert(aclPage.getContent()) );
            map.put( "currPage",currPage );
            map.put( "pageSize",pageSize );
            Integer from=(currPage-1)*pageSize+1;
            Integer to=from+aclPage.getContent().size()-1;
            map.put("from",from);
            map.put( "to",to );
            map.put( "totalCounts",aclPage.getTotalElements() );
            map.put( "totalPages",aclPage.getTotalPages() );
            return ResultVO.success( ResultEnum.SUCCESS.getMsg(),map );
        }catch(Exception ex){
            ex.printStackTrace();
            return ResultVO.fail( ex.getMessage() );
        }
    }

    @RequestMapping("/findById")
    @ResponseBody
    public ResultVO findById(@RequestParam("id") Integer id){
        if(id==null){
            return ResultVO.fail( ResultEnum.PARAM_IS_ERROR.getMsg() );
        }
        return ResultVO.success( ResultEnum.SUCCESS.getMsg(), aclService.findById( id ));
    }
}
