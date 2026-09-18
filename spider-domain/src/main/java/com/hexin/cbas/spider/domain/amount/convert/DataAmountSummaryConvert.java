package com.hexin.cbas.spider.domain.amount.convert;

import com.hexin.cbas.spider.dal.po.spider.DataAmountSummaryPO;
import com.hexin.cbas.spider.domain.amount.entity.DataAmountSummary;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

/**
 * @author wanghujia
 * @since 2021/8/10
 */
@Mapper(componentModel = "spring")
public interface DataAmountSummaryConvert {

    DataAmountSummaryPO toData(DataAmountSummary dataAmountSummary);

    DataAmountSummary toEntiy(DataAmountSummaryPO dataAmountSummaryPO);

}
