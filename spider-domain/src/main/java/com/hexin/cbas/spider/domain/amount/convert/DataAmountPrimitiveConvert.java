package com.hexin.cbas.spider.domain.amount.convert;

import com.hexin.cbas.spider.dal.po.spider.DataAmountPrimitivePO;
import com.hexin.cbas.spider.domain.amount.entity.DataAmountPrimitive;
import org.springframework.stereotype.Component;

/**
 * @author wanghujia
 * @since 2021/8/10
 */
@Component
public class DataAmountPrimitiveConvert {

    public DataAmountPrimitivePO toData(DataAmountPrimitive dataAmountPrimitive) {
        DataAmountPrimitivePO dataAmountPrimitivePO = new DataAmountPrimitivePO();
        dataAmountPrimitivePO.setId(dataAmountPrimitive.getId());
        dataAmountPrimitivePO.setChannelId(dataAmountPrimitive.getChannelId());
        dataAmountPrimitivePO.setInstanceId(dataAmountPrimitive.getInstanceId());
        dataAmountPrimitivePO.setPDate(dataAmountPrimitive.getPDate());
        dataAmountPrimitivePO.setLogAmount(dataAmountPrimitive.getLogAmount());
        dataAmountPrimitivePO.setUploadTime(dataAmountPrimitive.getUploadTime());
        return dataAmountPrimitivePO;
    }
}
