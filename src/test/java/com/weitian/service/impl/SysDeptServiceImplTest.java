package com.weitian.service.impl;

import com.weitian.service.SysDeptService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * Created by Administrator on 2018/11/29.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SysDeptServiceImplTest {
    @Autowired
    private SysDeptService deptService;
    @Test
    public void delete() throws Exception {
        deptService.delete( 18);
    }

}