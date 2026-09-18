package com.hexin.cbas.spider.domain.metacollection.repository.impl;

import com.hexin.cbas.spider.dal.mapper.metacollection.BizMapper;
import com.hexin.cbas.spider.domain.metacollection.repository.IBizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author wanghujia
 * @since 2021/9/10
 */
@Repository
public class BizRepositoryImpl implements IBizRepository {

    @Autowired
    private BizMapper bizMapper;

    @Override
    public List<String> getAllBizNames() {
        return bizMapper.getAllBizNames();
    }
}
