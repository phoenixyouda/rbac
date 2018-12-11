package com.weitian.service.impl;

import com.weitian.entity.SysLog;
import com.weitian.repository.SysLogRepository;
import com.weitian.service.SysLogService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Administrator on 2018/12/10.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SysLogServiceImplTest {
    @Autowired
    private SysLogService logService;
    @Autowired
    private SysLogRepository logRepository;
    @Test
    public void findAllByTypeOrOrOperator() throws Exception {

        SysLog log=new SysLog();


        Page<SysLog> page=logService.findAllByTypeOrOperator( 1,null,1,10 );
        System.out.println( page.getContent().size() );
    }

}