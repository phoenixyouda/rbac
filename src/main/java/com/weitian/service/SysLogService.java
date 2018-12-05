package com.weitian.service;

import com.weitian.entity.SysLog;

import java.util.List;

/**
 * Created by Administrator on 2018/11/22.
 */
public interface SysLogService {
    public SysLog save(SysLog sysLog);
    public void save(List<SysLog> sysLogList);
}
