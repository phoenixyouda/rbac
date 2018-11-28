package com.weitian.utils;

import com.google.gson.Gson;
import com.weitian.entity.SysDept;
import com.weitian.entity.SysLog;
import com.weitian.enums.LogEnum;

/**
 * Created by Administrator on 2018/11/28.
 */
public class SysDept2SysLogConvert {
    public static SysLog convert(SysDept oldDept, SysDept newDept, Integer updateType) {
        SysLog sysLog = new SysLog();


        Gson gson = new Gson();

        //操作表
        sysLog.setType( LogEnum.DEPARTMENT_TABLE.getTypeCode() );
        //记录Id
        sysLog.setTargetId( oldDept.getId() );
        //原始数据
        sysLog.setOldValue( gson.toJson( oldDept ) );
        sysLog.setNewValue( "" );
        sysLog.setStatus( LogEnum.UN_RESTORE.getStatusCode() );

        sysLog.setUpdateType( updateType );

        sysLog.setOperator( oldDept.getOperator() );
        sysLog.setOperatorIP( oldDept.getOperatorIP() );


        //更新操作
        if (LogEnum.OPERATOR_UPDATE.getOperatorCode().equals( updateType )) {
            sysLog.setNewValue( gson.toJson( newDept ) );
            sysLog.setOperator( newDept.getOperator() );
            sysLog.setOperatorIP( newDept.getOperatorIP() );
        }
        return sysLog;
    }
}
