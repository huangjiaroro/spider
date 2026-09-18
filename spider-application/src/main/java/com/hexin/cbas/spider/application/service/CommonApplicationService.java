package com.hexin.cbas.spider.application.service;

import com.hexin.cbas.spider.domain.metacollection.repository.IBizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wanghujia
 * @since 2021/9/10
 */
@Service
public class CommonApplicationService {

    @Autowired
    private IBizRepository bizRepository;

    public List<String> getAllBizNames() {
        return bizRepository.getAllBizNames();
    }
}
