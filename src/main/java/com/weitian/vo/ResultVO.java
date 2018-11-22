package com.weitian.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

/**
 * Created by Administrator on 2018/11/22.
 */
@Data
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ResultVO {
    private boolean rect;
    private String msg;
    private Object data;

    public static ResultVO success(String msg,Object data){
        ResultVO resultVO=new ResultVO();
        resultVO.setRect( true );
        resultVO.setMsg( msg );
        resultVO.setData( data );
        return resultVO;
    }
    public static ResultVO fail(String msg){
        ResultVO resultVO=new ResultVO();
        resultVO.setRect( false );
        resultVO.setMsg( msg );
        return resultVO;
    }


}
