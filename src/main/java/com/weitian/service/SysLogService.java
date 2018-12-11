package com.weitian.service;

import com.weitian.entity.SysLog;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created by Administrator on 2018/11/22.
 */
public interface SysLogService {
    public SysLog save(SysLog sysLog);
    public void save(List<SysLog> sysLogList);

    public Page<SysLog> findAllByTypeOrOperator(Integer type, String operator, Integer currPage,Integer pageSize);
    public Page<SysLog> findAll(Integer currPage,Integer pageSize);
}
