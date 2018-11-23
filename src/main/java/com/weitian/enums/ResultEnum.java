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
    ;
    private Integer code;
    private String msg;
}
