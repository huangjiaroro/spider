package com.hexin.cbas.spider.domain.channel.convert;

import com.hexin.cbas.spider.dal.po.spider.DataChannelPO;
import com.hexin.cbas.spider.domain.channel.entity.DataChannel;
import org.mapstruct.Mapper;

/**
 * @description:
 * @author: huangjiarong
 * @create: 2022/1/7
 * @version: v1.0
 */
@Mapper(componentModel = "spring")
public interface DataChannelConvert {

    DataChannelPO toData(DataChannel dataChannel);

    DataChannel fromData(DataChannelPO channelPO);
}
