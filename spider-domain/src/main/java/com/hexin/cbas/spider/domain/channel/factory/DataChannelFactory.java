package com.hexin.cbas.spider.domain.channel.factory;

import com.hexin.cbas.spider.dal.mapper.spider.DataChannelMapper;
import com.hexin.cbas.spider.domain.channel.convert.DataChannelConvert;
import com.hexin.cbas.spider.domain.channel.entity.DataChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author wanghujia
 * @since 2021/8/11
 */
@Component
public class DataChannelFactory {

    @Autowired
    private DataChannelMapper dataChannelMapper;

    @Autowired
    private DataChannelConvert dataChannelConvert;



    public DataChannel createDataChannel(Long id) {
        DataChannel dataChannel = dataChannelConvert.fromData(dataChannelMapper.selectByPrimaryKey(id));
        return dataChannel;
    }
}
