package com.weitian.service.impl;

import com.weitian.entity.SysLog;
import com.weitian.enums.ResultEnum;
import com.weitian.exception.ResultException;
import com.weitian.repository.SysLogRepository;
import com.weitian.service.SysLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2018/11/22.
 */
@Service
@Slf4j
public class SysLogServiceImpl implements SysLogService {
    @Autowired
    SysLogRepository sysLogRepository;
    @Override
    public SysLog save(SysLog sysLog) {
        SysLog logTemp=sysLogRepository.save( sysLog );

        if(null==logTemp){
            log.error( "【操作日志更新失败】,msg={},code={}", ResultEnum.OPERATOR_LOG_ERROR.getMsg(),ResultEnum.OPERATOR_LOG_ERROR.getCode() );
            throw new ResultException( ResultEnum.OPERATOR_LOG_ERROR );
        }
        return logTemp;
    }
    public void save(List<SysLog> sysLogList){
        sysLogRepository.save( sysLogList );
    }
}
