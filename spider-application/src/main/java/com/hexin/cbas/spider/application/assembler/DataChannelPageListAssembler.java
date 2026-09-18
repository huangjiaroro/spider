package com.hexin.cbas.spider.application.assembler;

import com.hexin.cbas.spider.application.dto.DataChannelPageListDTO;
import com.hexin.cbas.spider.dal.po.spider.DataChannelPO;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

/**
 * @author wanghujia
 * @since 2021/8/11
 */
@Mapper(componentModel = "spring")
public interface DataChannelPageListAssembler {

    DataChannelPageListDTO toDTO(DataChannelPO dataChannelPO);
}
