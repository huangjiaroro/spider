package com.hexin.cbas.spider.application.assembler;

import com.hexin.cbas.spider.application.dto.DataAmountPrimitiveDTO;
import com.hexin.cbas.spider.domain.amount.entity.DataAmountPrimitive;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author wanghujia
 * @since 2021/8/16
 */
@Component
public class DataAmountPrimitiveAssembler {

    public DataAmountPrimitive toEntity(DataAmountPrimitiveDTO dataAmountPrimitiveDTO) {
        DataAmountPrimitive dataAmountPrimitive = new DataAmountPrimitive();
        dataAmountPrimitive.setChannelId(dataAmountPrimitiveDTO.getChannelId());
        dataAmountPrimitive.setInstanceId(dataAmountPrimitiveDTO.getInstanceId());
        dataAmountPrimitive.setPDate(dataAmountPrimitiveDTO.getDateStr());
        dataAmountPrimitive.setLogAmount(dataAmountPrimitiveDTO.getLogAmount());
        dataAmountPrimitive.setUploadTime(new Date());
        return dataAmountPrimitive;
    }
}
