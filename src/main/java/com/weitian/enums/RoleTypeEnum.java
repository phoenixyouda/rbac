package com.weitian.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by Administrator on 2018/12/6.
 */
@AllArgsConstructor
@Getter
public enum RoleTypeEnum implements CodeEnum {
    TYPE_OF_ADMIN(1,"管理员"),
    TYPE_OF_OTHER(2,"其他"),
    ;
    private Integer code;
    private String msg;
}
