package com.hexin.cbas.spider.domain.channel.repository.impl;

import com.alibaba.fastjson.JSON;
import com.hexin.cbas.spider.dal.mapper.spider.ChannelNodeMapper;
import com.hexin.cbas.spider.dal.mapper.spider.DataChannelMapper;
import com.hexin.cbas.spider.dal.po.spider.ChannelNodePO;
import com.hexin.cbas.spider.dal.po.spider.DataChannelPO;
import com.hexin.cbas.spider.domain.channel.convert.DataChannelConvert;
import com.hexin.cbas.spider.domain.channel.entity.DataChannel;
import com.hexin.cbas.spider.domain.channel.factory.DataChannelFactory;
import com.hexin.cbas.spider.domain.channel.repository.DataChannelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author wanghujia
 * @since 2021/8/11
 */
@Repository
public class DataChannelRepositoryImpl implements DataChannelRepository {

    @Autowired
    private DataChannelConvert dataChannelConvert;

    @Autowired
    private DataChannelMapper dataChannelMapper;


    @Autowired
    private DataChannelFactory dataChannelFactory;

    @Autowired
    private ChannelNodeMapper channelNodeMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(DataChannel dataChannel) {
        DataChannelPO dataChannelPO = dataChannelConvert.toData(dataChannel);
        dataChannelPO.setKeyword(JSON.toJSONString(dataChannel));
        if (Objects.isNull(dataChannel.getId())) {
            dataChannelMapper.insert(dataChannelPO);
            dataChannel.setId(dataChannelPO.getId());
            if (CollectionUtils.isEmpty(dataChannel.getNodes())) {
                return;
            }
            dataChannel.getNodes().forEach(e -> {
                e.setId(null);
                e.setChannelId(dataChannelPO.getId().intValue());
                channelNodeMapper.insertSelective(e);
            });
        } else {
            //更新channel
            dataChannelMapper.updateByPrimaryKeySelective(dataChannelPO);
            //保存时进行节点的校验删除和添加
            List<Integer> oldIds = channelNodeMapper.selectByChannelId(Math.toIntExact(dataChannelPO.getId())).stream()
                    .map(ChannelNodePO::getId).collect(Collectors.toList());
            List<Integer> newIds = dataChannel.getNodes().stream().map(ChannelNodePO::getId).collect(Collectors.toList());
            //对不存在的点进行删除
            oldIds.removeAll(newIds);
            oldIds.forEach(channelNodeMapper::deleteByPrimaryKey);
            dataChannel.getNodes().forEach(e -> {
                if (e.getId() == null) {
                    e.setChannelId(dataChannelPO.getId().intValue());
                    channelNodeMapper.insertSelective(e);
                } else {
                    channelNodeMapper.updateByPrimaryKeySelective(e);
                }
            });
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(Long id) {
        dataChannelMapper.deleteByPrimaryKey(id);
        channelNodeMapper.deleteByChannelId(id);
    }

    @Override
    public DataChannel find(Long id) {
        return dataChannelFactory.createDataChannel(id);
    }

    @Override
    public void changeState(Long id, String state) {
        dataChannelMapper.changeState(id, state);
    }

    @Override
    public void changeMetric(Long id, Integer metric) {
        dataChannelMapper.changeMetric(id, metric);
    }
}
