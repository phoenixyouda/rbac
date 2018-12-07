package com.weitian.repository;

import com.weitian.entity.SysAcl;
import com.weitian.entity.SysAclModule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;



import static org.junit.Assert.*;

/**
 * Created by Administrator on 2018/12/6.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SysAclRepositoryTest {
    @Autowired
    private SysAclRepository aclRepository;
    @Test
    public void findAllBySysAclModule() throws Exception {
        SysAclModule module=new SysAclModule();
        module.setId( 2 );
        Pageable pageable=new PageRequest( 1,10 );
        Page<SysAcl> page=aclRepository.findAllBySysAclModule( pageable,module );
        System.out.println( page.getContent().size() );
    }

}