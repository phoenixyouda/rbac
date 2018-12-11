package com.weitian.repository;

import com.weitian.entity.SysLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


/**
 * Created by Administrator on 2018/11/22.
 */
public interface SysLogRepository extends JpaRepository<SysLog,Integer>,JpaSpecificationExecutor {
}
