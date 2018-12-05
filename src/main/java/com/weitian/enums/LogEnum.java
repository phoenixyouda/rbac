package com.weitian.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by Administrator on 2018/11/22.
 */

@Getter
public enum LogEnum {


    UNKNOWN_TABLE(0,"未知表"),
    DEPARTMENT_TABLE(1,"部门表"),
    USER_TABLE(2,"用户表"),
    ACL_MODULE_TABLE(3,"权限模块表"),
    ACL_TABLE(4,"权限表"),
    ROLE_TABLE(5,"角色表"),
    ROLE_USER_TABLE(6,"角色用户关系表"),
    ROLE_ACL_TABLE(7,"角色权限关系表"),
    


    RESTORE_FINISH("已还原",1),
    UN_RESTORE("未还原",0),



    OPERATOR_SAVE("增加",0,""),
    OPERATOR_UPDATE("更新",1,""),
    OPERATOR_DELETE("删除",2,""),
    OPERATOR_GRANT("授予",3,""),
    OPERATOR_REVOKE("撤销",4,""),



    ;
    /*操作目标表: 0：未知表，不做处理 1：部门表 2：用户表 3：权限模块表 4：权限表 5：角色表 6：角色用户关系表 7 角色权限关系表*/
    private Integer tableCode;
    private String tableName;
    /*操作状态 0 :未还原 1：已还原*/
    private Integer statusCode;
    private String statusName;

    /*操作类型，0，增加，1，修改，2，删除，3，授予，4，撤销*/
    private Integer operatorCode;
    private String operatorName;

    LogEnum(String operatorName,Integer operatorCode,String operator){
        this.operatorCode=operatorCode;
        this.operatorName=operatorName;
    }



    LogEnum(String statusName,Integer status){
        this.statusName=statusName;
        this.statusCode=status;
    }
    LogEnum(Integer tableCode, String tableName) {
        this.tableCode = tableCode;
        this.tableName = tableName;
    }
}
