package com.weitian.service.impl;

import com.weitian.entity.SysDept;
import com.weitian.entity.SysUser;
import com.weitian.service.SysDeptService;
import com.weitian.service.SysUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Administrator on 2018/11/29.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SysDeptServiceImplTest {
    @Autowired
    private SysDeptService deptService;
    @Autowired
    private SysUserService userService;




    @Test
    public void test() throws Exception{
        List<SysDept> deptList=deptService.findAllDept();
        List<SysUser> userList=userService.findAll();
        System.out.println( "缓存删除" );
        deptService.delete( 50 );
    }

}