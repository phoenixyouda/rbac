package com.weitian.controller;

import com.sun.javafx.scene.control.skin.VirtualFlow;
import com.weitian.convert.SysAclModuleConverter;
import com.weitian.convert.SysRoleConverter;
import com.weitian.dto.SysAclModuleDto;
import com.weitian.dto.SysRoleDto;
import com.weitian.entity.*;
import com.weitian.enums.ResultEnum;
import com.weitian.form.RoleForm;
import com.weitian.service.*;
import com.weitian.utils.TreeUtils;
import com.weitian.vo.ResultVO;
import freemarker.template.utility.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2018/12/6.
 */
@Controller
@RequestMapping("/sys/role")
public class SysRoleController {

    @Autowired
    private SysRoleService roleService;
    @Autowired
    private SysAclService aclService;
    @Autowired
    private SysUserService userService;
    @Autowired
    private SysRoleAclService roleAclService;
    @Autowired
    private SysRoleUserService roleUserService;

    @Autowired
    private SysAclModuleService moduleService;



    /**
     * 新增角色用户
     * @param roleId
     * @param userIds
     * @return
     */
    @RequestMapping("/changeUsers")
    @ResponseBody
    public ResultVO changeUsers(@RequestParam("roleId") Integer roleId,@RequestParam("userIds") String userIds){

        if(null==roleId){
            return ResultVO.fail( ResultEnum.PARAM_IS_ERROR.getMsg() );
        }

        if(StringUtils.isEmpty( userIds )){
            return ResultVO.fail( ResultEnum.PARAM_IS_ERROR.getMsg() );
        }

        List<String> ids=Arrays.asList( userIds.split( "," ));
        try {
            roleUserService.deleteByRoleId( roleId );
            roleUserService.save( roleId ,ids);
        }catch (Exception ex){
            return ResultVO.fail( ex.getMessage() );
        }
        return ResultVO.success( ResultEnum.SUCCESS.getMsg() );
    }


    /**
     * 根据角色显示所属用户及非角色用户
     * @param roleId
     * @return
     */
    @RequestMapping("/users")
    @ResponseBody
    public ResultVO findUsers(@RequestParam("roleId") Integer roleId){
        if(null==roleId){
            return ResultVO.fail( ResultEnum.PARAM_IS_ERROR.getMsg() );
        }

        SysRole sysRole=roleService.findOne( roleId );
        List<SysRoleUser> roleUserList=roleUserService.findByRoleId( sysRole.getId() );
        List<Integer> userIds=roleUserList.stream().map( e->e.getUserId() ).collect( Collectors.toList() );

        List<SysUser> selectedUsers=userService.findAllByIdIn( userIds );

        List<SysUser> allUsers=userService.findAll();

        List<SysUser> unSelectedUsers= new ArrayList<>(  );
        for(SysUser sUser:selectedUsers) {
            allUsers.remove( sUser );
        }


        Map<String,List<SysUser>> map=new HashMap<>(  );
        map.put("selected", selectedUsers);
        map.put("unselected", allUsers);
        return ResultVO.success( ResultEnum.SUCCESS.getMsg(), map);
    }

    /**
     * 根据角色显示权限树列表
     *
     * @param roleId
     * @return
     */
    @RequestMapping("/roleTree")
    @ResponseBody
    public ResultVO findModule(@RequestParam("roleId") Integer roleId) {

        if (null == roleId) {
            return ResultVO.fail( ResultEnum.PARAM_IS_ERROR.getMsg() );
        }
        SysRole sysRole = roleService.findOne( roleId );
        if (null == sysRole) {
            return ResultVO.fail( ResultEnum.ROLE_NOT_EXISTS.getMsg() );
        }
        //查询所有模块及权限
        List<SysAclModule> aclModuleList = moduleService.findAllAclModule();
        //该角色拥有的权限checked设为true,便于前端树形图显示
        roleService.setAclsByRole( sysRole, aclModuleList );
        return ResultVO.success( ResultEnum.SUCCESS.getMsg(), aclModuleList );
    }

    /**
     * 新增角色权限
     * @param roleId
     * @param aclIds
     * @return
     */
    @RequestMapping("/changeAcls")
    @ResponseBody
    public ResultVO changeAcls(@RequestParam("roleId") Integer roleId,@RequestParam("aclIds") String aclIds){
        if(StringUtils.isEmpty(aclIds)){
            return ResultVO.fail( ResultEnum.PARAM_IS_ERROR.getMsg() );
        }

        if(null==roleId){
            return ResultVO.fail( ResultEnum.PARAM_IS_ERROR.getMsg() );
        }
        List<String> idList= Arrays.asList( aclIds.split( ","  ));
        List<SysAcl> sysAclList=new ArrayList<>(  );
        try {
            for (String id : idList) {
                sysAclList.add( aclService.findById( Integer.parseInt( id ) ) );
            }
            SysRole sysRole = roleService.findOne( roleId );

            roleAclService.save( sysRole, sysAclList );
            return ResultVO.success( ResultEnum.SUCCESS.getMsg() );
        }catch(Exception ex){
            return ResultVO.fail( ex.getMessage() );
        }
    }

    /**
     * 删除权限
     *
     * @param id
     * @return
     */
    @RequestMapping("/delete")
    @ResponseBody
    public ResultVO delete(@RequestParam("id") Integer id) {
        if (id == null) {
            return ResultVO.fail( ResultEnum.PARAM_IS_ERROR.getMsg() );
        }
        try {
            roleService.delete( id );
            return ResultVO.success( ResultEnum.SUCCESS.getMsg() );
        } catch (Exception ex) {
            return ResultVO.fail( ex.getMessage() );
        }
    }

    /**
     * 新增、更新角色
     *
     * @param roleForm
     * @param bindingResult
     * @return
     */
    @RequestMapping("/save")
    @ResponseBody
    public ResultVO save(@Valid RoleForm roleForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResultVO.fail( bindingResult.getFieldError().getDefaultMessage() );
        }
        SysRoleDto roleDto = SysRoleConverter.convert( roleForm );
        try {
            if (roleDto.getId() == null) {
                return ResultVO.success( ResultEnum.SUCCESS.getMsg(), roleService.save( roleDto ) );
            } else {
                return ResultVO.success( ResultEnum.SUCCESS.getMsg(), roleService.update( roleDto ) );
            }
        } catch (Exception e) {
            return ResultVO.fail( e.getMessage() );
        }
    }


    @RequestMapping("/roleList")
    @ResponseBody
    public ResultVO roleList() {
        try {
            List<SysRoleDto> roleDtoList = roleService.findAll();
            return ResultVO.success( ResultEnum.SUCCESS.getMsg(), roleDtoList );
        } catch (Exception ex) {
            return ResultVO.fail( ex.getMessage() );
        }
    }


    @RequestMapping("/show")
    public String show() {
        return "role/role";
    }
}
