package com.weitian.service.impl;

import com.weitian.entity.SysRole;
import com.weitian.entity.SysRoleUser;
import com.weitian.entity.SysUser;
import com.weitian.repository.SysRoleRepository;
import com.weitian.repository.SysRoleUserRepository;
import com.weitian.service.SysRoleService;
import com.weitian.service.SysRoleUserService;
import com.weitian.service.SysUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    private SysRoleService roleService;
    @Autowired
    private SysRoleRepository roleRepository;
    @Autowired
    private SysRoleUserService roleUserService;
    @Autowired
    private SysRoleUserRepository roleUserRepository;
    @Test
    public void test(){
//        userService.findById( null );
//        //SysUser user=userService.findById( 0 );
//        //roleRepository.findOne( 0 );

        SysRole sysRole=roleService.findOne( 1 );
        List<SysRoleUser> roleUserList=roleUserRepository.findByRoleId( sysRole.getId() );
        List<Integer> userIds=roleUserList.stream().map( e->e.getUserId() ).collect( Collectors.toList() );

        List<SysUser> selectedUsers=userService.findAllByIdIn( userIds );

        List<SysUser> allUsers=userService.findAll();

        List<SysUser> unSelectedUsers= new ArrayList<>(  );
        for(SysUser sysUser:allUsers){
            for(SysUser sUser:selectedUsers){
                if(!sUser.getId().equals( sysUser.getId() )){
                    unSelectedUsers.add( sysUser );
                }
            }
        }
        System.out.println( allUsers.size() );
        System.out.println( selectedUsers.size() );
        System.out.println( unSelectedUsers.size() );
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