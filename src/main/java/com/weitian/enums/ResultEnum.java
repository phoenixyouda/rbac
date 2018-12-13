package com.weitian.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Administrator on 2018/11/22.
 */
@AllArgsConstructor
@Getter
public enum ResultEnum {

    SUCCESS(200,"成功"),
    LOGIN_SUCCESS(201,"成功登录"),
    LOGOUT_SUCCESS(202,"成功注销"),
    REQUESTDATA_NOT_EXISTS(501,"请求数据不存在"),

    PARAM_IS_ERROR(502,"请求参数不正确"),
    USERNAME_OR_PASSWORD_NOTNULL(503,"用户名或密码不得为空"),
    USERNAME_OR_PASSWORD_IS_ERROR(504,"用户名或密码错误"),
    /*部门异常*/
    PARENTDEPT_NOT_EXISTS(300,"父部门不存在"),
    DEPARTMENT_NOT_EXISTS(301,"部门不存在"),
    DEPARMENTNAME_IS_EXISTS(302,"部门名称重复"),
    DEPARTMENT_INSERT_ERROR(303,"部门新增失败"),
    DEPARTMENT_UPDATE_ERROR(304,"部门更新失败"),
    DEPARTMENT_CONTAINS_USERS(305,"部门中存在人员"),
    DEPARTMENT_CONTAINS_CHILDDEPTS(306,"部门下包含子部门"),

    /*员工异常*/
    USER_INSERT_ERROR(420,"新增员工失败"),
    USER_DELETE_ERROR(421,"删除员工失败"),
    USER_NOT_EXIST(422,"员工不存在"),
    USER_UPDATE_ERROR(423,"员工更新失败"),

    /*权限模块异常*/
    ACL_MODULE_INSERT_ERROR(530,"新增权限模块失败"),
    ACL_MODULE_UPDATE_ERROR(531,"修改权限模块失败"),
    ACL_MODULE_DELETE_ERROR(532,"删除权限模块失败"),
    ACL_MODULE_NOT_EXISTS(533,"权限模块不存在"),
    ACL_MODULE_IS_EXISTS(534,"模块名称重复"),
    MODULE_CONTAINS_CHILDMODULES(535,"模块下包含子模块"),
    MODULE_CONTAINS_ACLS(536,"模块下包含权限点"),

    /*权限点异常*/

    ACL_INSERT_ERROR(600,"新增权限点失败"),
    ACL_UPDATE_ERROR(601,"修改权限点失败"),
    ACL_DELETE_ERROR(602,"删除权限点失败"),
    ACL_IS_EXISTS(603,"权限点名称重复"),
    ACL_IS_USED(604," 该权限点正在使用中"),
    ACL_NOT_EXISTS(605,"该权限不存在"),


    /*角色异常*/
    ROLE_IS_EXISTS(700,"角色名称重复"),
    ROLE_INSERT_ERROR(701,"新增角色失败"),
    ROLE_UPDATE_ERROR(702,"修改角色失败"),
    ROLE_DELETE_ERROR(703,"删除角色失败"),
    ROLE_IS_USED(704,"该角色正在使用中"),
    ROLE_NOT_EXISTS(705,"该角色不存在"),


    /*操作日志异常*/
    OPERATOR_LOG_ERROR(900,"操作日志新增失败"),
    ;
    private Integer code;
    private String msg;
}
