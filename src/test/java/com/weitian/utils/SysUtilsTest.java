package com.weitian.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * Created by Administrator on 2018/12/6.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SysUtilsTest {

    @Test
    public void getCodeForAcl() throws Exception {
        System.out.println( SysUtils.getCodeForAcl().length() );
    }

}