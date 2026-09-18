package com.hexin.cbas.spider.domain.amount.service;

import com.hexin.cbas.spider.domain.amount.entity.DataAmountPrimitive;
import com.hexin.cbas.spider.domain.amount.entity.DataAmountSummary;
import com.hexin.cbas.spider.domain.amount.repository.DataAmountPrimitiveRepository;
import com.hexin.cbas.spider.domain.amount.repository.DataAmountSummaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wanghujia
 * @since 2021/8/16
 */
@Service
public class DataAmountDomainService {

    @Autowired
    private DataAmountPrimitiveRepository dataAmountPrimitiveRepository;

    @Autowired
    private DataAmountSummaryRepository dataAmountSummaryRepository;

    public void add(DataAmountPrimitive dataAmountPrimitive) {
        dataAmountPrimitiveRepository.save(dataAmountPrimitive);
    }

    public void save(DataAmountSummary dataAmountSummary) {
        dataAmountSummaryRepository.save(dataAmountSummary);
    }
}
