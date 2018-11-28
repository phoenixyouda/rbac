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
    REQUESTDATA_NOT_EXIST(501,"请求数据不存在"),
    PARAM_IS_ERROR(502,"请求参数不正确"),
    /*部门异常*/
    PARENTDEPT_NOT_EXIST(300,"父部门不存在"),
    DEPARMENTNAME_IS_EXISTS(301,"部门名称重复"),
    DEPARTMENT_INSERT_ERROR(302,"部门新增失败"),
    DEPARTMENT_UPDATE_ERROR(303,"部门更新失败"),

    /*操作日志异常*/
    OPERATOR_LOG_ERROR(900,"操作日志新增失败"),
    ;
    private Integer code;
    private String msg;
}
