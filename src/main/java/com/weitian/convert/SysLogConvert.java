package com.weitian.convert;

import ch.qos.logback.classic.net.SyslogAppender;
import com.google.gson.Gson;
import com.weitian.dto.SysLogDto;
import com.weitian.dto.SysUserDto;
import com.weitian.entity.SysDept;
import com.weitian.entity.SysLog;
import com.weitian.enums.LogEnum;
import com.weitian.utils.SysUtils;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2018/11/28.
 */
public class SysLogConvert {

    public static SysLogDto convert(SysLog sysLog){
        SysLogDto logDto=new SysLogDto();
        BeanUtils.copyProperties( sysLog,logDto );
        logDto.setStatusName( SysUtils.getByStatusCode( sysLog.getStatus() ).getStatusName() );
        logDto.setTableName( SysUtils.getByTableCode( sysLog.getType() ).getTableName() );
        logDto.setUpdateTypeName( SysUtils.getByOperatorCode( sysLog.getUpdateType() ).getOperatorName() );

        return logDto;
    }

    public static List<SysLogDto> convert(List<SysLog> logList){
        return logList.stream().map( e->convert(e) ).collect( Collectors.toList() );
    }


    //增，删转log
    public static SysLog convertForCD(Integer id,String oldValue,Integer tableCode, Integer updateType) {
        SysLog sysLog = new SysLog();
        //操作表
        sysLog.setType( tableCode);
        //记录Id
        sysLog.setTargetId( id );
        //原始数据
        sysLog.setOldValue( oldValue );
        sysLog.setNewValue( "" );
        sysLog.setStatus( LogEnum.UN_RESTORE.getStatusCode() );

        sysLog.setUpdateType( updateType );

        sysLog.setOperator( SysUtils.getSessionUserName() );
        sysLog.setOperatorIP( SysUtils.getSessionUserIp());
        return sysLog;
    }
    public static SysLog convertForUpdate(Integer id,String oldValue,String newValue,Integer tableType) {
        SysLog sysLog = new SysLog();

        //操作表
        sysLog.setType( tableType );
        //记录Id
        sysLog.setTargetId( id );
        //原始数据
        sysLog.setOldValue( oldValue);
        sysLog.setNewValue( newValue );
        //未还原
        sysLog.setStatus( LogEnum.UN_RESTORE.getStatusCode() );
        //操作类型：更新
        sysLog.setUpdateType( LogEnum.OPERATOR_UPDATE.getOperatorCode() );

        sysLog.setOperator( SysUtils.getSessionUserName());
        sysLog.setOperatorIP( SysUtils.getSessionUserIp() );
        return sysLog;
    }
}
