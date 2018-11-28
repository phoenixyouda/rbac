package com.weitian.controller;

import com.weitian.dto.SysDeptDto;
import com.weitian.entity.SysDept;
import com.weitian.enums.ResultEnum;
import com.weitian.exception.ResultException;
import com.weitian.form.DeptForm;
import com.weitian.service.SysDeptService;
import com.weitian.utils.TreeUtils;
import com.weitian.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by Administrator on 2018/11/25 0025.
 */
@Controller
@RequestMapping("/sys/dept")
@Slf4j
public class SysDeptController {
    @Autowired
    private SysDeptService deptService;




    @RequestMapping("/update")
    @ResponseBody
    public ResultVO update(@Valid DeptForm deptForm,BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new ResultException( ResultEnum.PARAM_IS_ERROR );
        }
        SysDeptDto deptDto=new SysDeptDto();
        BeanUtils.copyProperties( deptForm,deptDto );
        try {
            return ResultVO.success( ResultEnum.SUCCESS.getMsg(), deptService.update( deptDto ));
        } catch (Exception e) {
        }
        return ResultVO.fail( ResultEnum.DEPARTMENT_UPDATE_ERROR.getMsg());
    }

    @RequestMapping("/save")
    @ResponseBody
    public ResultVO save(@Valid DeptForm deptForm , BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new ResultException( ResultEnum.PARAM_IS_ERROR );
        }
        SysDeptDto deptDto=new SysDeptDto();
        BeanUtils.copyProperties( deptForm,deptDto );
        try {
            return ResultVO.success( ResultEnum.SUCCESS.getMsg(), deptService.save( deptDto ));
        } catch (Exception e) {
        }
        return ResultVO.fail( ResultEnum.DEPARTMENT_INSERT_ERROR.getMsg());
    }
    @RequestMapping("/tree")
    @ResponseBody
    public ResultVO queryAllDept(){

        try {
            List<SysDept> deptList=deptService.findAllDept();
            List<SysDeptDto> deptDtoList = TreeUtils.findAll( deptList );
            //return ResultVO.fail( "123" );
            return ResultVO.success( ResultEnum.SUCCESS.getMsg(),deptDtoList );
        } catch (Exception e) {
            return ResultVO.fail( e.getMessage() );
        }
    }
    @RequestMapping("/show")
    public String show(){
        return "dept/dept";
    }
}
