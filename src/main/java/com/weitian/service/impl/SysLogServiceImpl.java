package com.weitian.service.impl;

import com.weitian.entity.SysLog;
import com.weitian.enums.ResultEnum;
import com.weitian.exception.ResultException;
import com.weitian.repository.SysLogRepository;
import com.weitian.service.SysLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/11/22.
 */
@Service
@Slf4j
public class SysLogServiceImpl implements SysLogService {



    @Autowired
    SysLogRepository sysLogRepository;

    @Override
    public Page<SysLog> findAll(Integer currPage, Integer pageSize) {
        Pageable pageable=new PageRequest( currPage,pageSize,this.GetSort() );
        return sysLogRepository.findAll( pageable );
    }


    public Sort GetSort(){
        List<Sort.Order> sortList=new ArrayList<>(  );
        sortList.add( new Sort.Order( Sort.Direction.DESC ,"operatorTime") );
        sortList.add( new Sort.Order( Sort.Direction.DESC ,"operator") );
        sortList.add( new Sort.Order( Sort.Direction.ASC ,"type") );
        sortList.add( new Sort.Order( Sort.Direction.ASC ,"updateType") );
        Sort sort=new Sort(sortList);
        return sort;
    }

    @Override
    public Page<SysLog> findAllByTypeOrOperator(Integer type, String operator, Integer currPage,Integer pageSize) {


        Pageable pageable=new PageRequest( currPage ,pageSize, this.GetSort());

        Specification<SysLog> specification=new Specification<SysLog>() {
            @Override
            public Predicate toPredicate(Root<SysLog> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

                List<Predicate> predicateList=new ArrayList<>(  );
                if(0!=type) {
                    predicateList.add(cb.equal( root.get( "type" ), type ));
                }
                if(!StringUtils.isEmpty(operator)) {
                    predicateList.add( cb.like( root.get( "operator" ), "%" + operator + "%" ));
                }

                return cb.and( predicateList.toArray( new Predicate[predicateList.size()] ) );
            }
        };

        return sysLogRepository.findAll( specification,pageable );
    }

    @Override
    public SysLog save(SysLog sysLog) {
        SysLog logTemp=sysLogRepository.save( sysLog );

        if(null==logTemp){
            log.error( "【操作日志更新失败】,msg={},code={}", ResultEnum.OPERATOR_LOG_ERROR.getMsg(),ResultEnum.OPERATOR_LOG_ERROR.getCode() );
            throw new ResultException( ResultEnum.OPERATOR_LOG_ERROR );
        }
        return logTemp;
    }
    public void save(List<SysLog> sysLogList){
        sysLogRepository.save( sysLogList );
    }
}
