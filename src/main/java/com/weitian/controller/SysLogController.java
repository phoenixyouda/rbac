package com.weitian.controller;

import ch.qos.logback.classic.net.SyslogAppender;
import com.weitian.convert.SysLogConvert;
import com.weitian.convert.SysUserConverter;
import com.weitian.entity.SysLog;
import com.weitian.enums.ResultEnum;
import com.weitian.service.SysLogService;
import com.weitian.vo.ResultVO;
import org.hibernate.transform.ResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/12/10.
 */
@RequestMapping("/sys/log")
@Controller
public class SysLogController {
    @Autowired
    private SysLogService logService;
    @RequestMapping("/show")
    public String show(){
        return "log/log";
    }

    @RequestMapping("/page")
    @ResponseBody
    public ResultVO page(@RequestParam(name="type",defaultValue = "0") Integer type, @RequestParam("operator") String operator, @RequestParam(name="currPage",defaultValue = "1") Integer currPage, @RequestParam(name="pageSize",defaultValue = "10") Integer pageSize){
        Map<String,Object> map=new HashMap<>(  );
        Page<SysLog> page=null;
        try {
            if (0 != type || !StringUtils.isEmpty( operator )) {
                page=logService.findAllByTypeOrOperator( type, operator, currPage-1, pageSize );
            } else {
                page=logService.findAll( currPage-1, pageSize );
            }
            map.put( "list", SysLogConvert.convert(page.getContent()) );
            map.put( "currPage",currPage );
            map.put( "pageSize",pageSize );
            Integer from=(currPage-1)*pageSize+1;
            Integer to=from+page.getContent().size()-1;
            map.put("from",from);
            map.put( "to",to );
            map.put( "totalCounts",page.getTotalElements() );
            map.put( "totalPages",page.getTotalPages() );

        }catch(Exception ex){
            return ResultVO.fail( ex.getMessage());
        }
        return ResultVO.success( ResultEnum.SUCCESS.getMsg(), map);
    }
}
