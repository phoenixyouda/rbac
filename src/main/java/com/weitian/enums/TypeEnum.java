package com.weitian.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by Administrator on 2018/12/6.
 */
@AllArgsConstructor
@Getter
public enum TypeEnum implements CodeEnum {
    TYPE_FOR_MENU(1,"菜单"),
    TYPE_FOR_BUTTON(2,"按钮"),
    TYPE_FOR_OTHER(3,"其他"),
    ;
    private Integer code;
    private String msg;
}
