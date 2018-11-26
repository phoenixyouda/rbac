package com.weitian.utils;

import com.weitian.dto.SysDeptDto;
import com.weitian.entity.SysDept;
import com.weitian.repository.SysDeptRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Administrator on 2018/11/26.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TreeUtilsTest {
    @Autowired
    private SysDeptRepository deptRepository;

    @Test
    public void deptTree() throws Exception {
        List<SysDept> deptList=deptRepository.findAll();
        List<SysDeptDto> deptDtoList=TreeUtils.findAll( deptList );
        System.out.println(deptDtoList);
    }

}