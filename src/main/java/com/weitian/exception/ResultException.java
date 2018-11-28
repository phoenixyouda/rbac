package com.weitian.exception;

import com.weitian.enums.ResultEnum;
import lombok.Data;
/**
 * Created by Administrator on 2018/11/22.
 */
@Data
public class ResultException extends  RuntimeException {
    private Integer code;

    public ResultException(ResultEnum resultEnum){
        super(resultEnum.getMsg());
        this.code=resultEnum.getCode();
    }

}
