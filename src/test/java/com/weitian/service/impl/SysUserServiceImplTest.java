package com.weitian.service.impl;

import com.weitian.entity.SysUser;
import com.weitian.repository.SysRoleRepository;
import com.weitian.service.SysUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Administrator on 2018/11/22.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SysUserServiceImplTest {
    @Autowired
    private SysUserService userService;
    @Autowired
    private SysRoleRepository roleRepository;
    @Test
    public void test(){
        userService.findById( null );
        //SysUser user=userService.findById( 0 );
        //roleRepository.findOne( 0 );
    }

    @Test
    public void testAll(){
        List<SysUser> list=userService.findAll( 1, 10,1 ).getContent();
        for(SysUser user:list){
            System.out.println( user.getUsername() );

            System.out.println( user.getSysDept().getName() );
        }
    }

}