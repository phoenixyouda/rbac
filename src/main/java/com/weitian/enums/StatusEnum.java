package com.weitian.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by Administrator on 2018/11/30.
 */
@Getter
@AllArgsConstructor
public enum StatusEnum implements  CodeEnum{
    STATUS_OF_FREEZE(0,"冻结"),
    STATUS_OF_NORMAL(1,"正常"),
    STATUS_OF_DELETED(2,"删除"),
    ;
    private Integer code;
    private String message;




}
