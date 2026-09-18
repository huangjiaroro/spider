package com.hexin.cbas.spider.application.service;

import com.hexin.cbas.spider.application.assembler.DataAmountPrimitiveAssembler;
import com.hexin.cbas.spider.application.dto.DataAmountPrimitiveDTO;
import com.hexin.cbas.spider.domain.amount.entity.DataAmountSummary;
import com.hexin.cbas.spider.domain.amount.service.DataAmountDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author wanghujia
 * @since 2021/8/11
 */
@Service
public class DataAmountApplicationService {

    @Autowired
    private DataAmountDomainService dataAmountDomainService;

    @Autowired
    private DataAmountPrimitiveAssembler dataAmountPrimitiveAssembler;

    @Transactional(rollbackFor = Exception.class)
    public void add(DataAmountPrimitiveDTO dataAmountPrimitiveDTO) {
        dataAmountDomainService.add(dataAmountPrimitiveAssembler.toEntity(dataAmountPrimitiveDTO));
    }

    @Transactional(rollbackFor = Exception.class)
    public void save(DataAmountSummary dataAmountSummary) {
        dataAmountDomainService.save(dataAmountSummary);
    }
}
