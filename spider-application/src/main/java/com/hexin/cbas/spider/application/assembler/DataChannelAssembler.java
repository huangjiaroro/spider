package com.hexin.cbas.spider.application.assembler;

import com.hexin.cbas.spider.application.dto.ChannelGraphDTO;
import com.hexin.cbas.spider.application.dto.DataChannelDTO;
import com.hexin.cbas.spider.dal.po.spider.ChannelNodePO;
import com.hexin.cbas.spider.dal.po.spider.DataChannelPO;
import com.hexin.cbas.spider.domain.channel.entity.DataChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author wanghujia
 * @since 2021/8/11
 */
@Component
public class DataChannelAssembler {

    @Autowired
    DataChannelGraphAssembler channelGraphAssembler;

    public ChannelGraphDTO toDTO(DataChannelPO dataChannelPO) {
        ChannelGraphDTO channelGraphDTO = new ChannelGraphDTO();
        channelGraphDTO.setChannelId(dataChannelPO.getId().intValue());
        channelGraphDTO.setName(dataChannelPO.getName());
        channelGraphDTO.setBizLine(dataChannelPO.getBizLine());
        channelGraphDTO.setDescription(dataChannelPO.getDescription());
        channelGraphDTO.setDataFormat(dataChannelPO.getDataFormat());
        channelGraphDTO.setMetric(dataChannelPO.getMetric());
        channelGraphDTO.setMetricDayType(dataChannelPO.getMetricDayType());
        channelGraphDTO.setMetricEnd(dataChannelPO.getMetricEnd());
        channelGraphDTO.setMetricStart(dataChannelPO.getMetricStart());
        channelGraphDTO.setMetricInterval(dataChannelPO.getMetricInterval());
        channelGraphDTO.setArrivalRate(dataChannelPO.getArrivalRate());
        channelGraphDTO.setEarlierRate(dataChannelPO.getEarlierRate());
        channelGraphDTO.setAlertThreshold(dataChannelPO.getAlertThreshold());
        channelGraphDTO.setIntervalType(dataChannelPO.getIntervalType());
        channelGraphDTO.setIntegrityCheckFlag(dataChannelPO.getIntegrityCheckFlag());
        return channelGraphDTO;
    }

    public DataChannelPO toPO(ChannelGraphDTO channelGraphDTO) {
        DataChannelPO dataChannelPO = new DataChannelPO();
        dataChannelPO.setId(channelGraphDTO.getChannelId().longValue());
        dataChannelPO.setMetric(channelGraphDTO.getMetric());
        dataChannelPO.setMetricDayType(channelGraphDTO.getMetricDayType());
        dataChannelPO.setMetricEnd(channelGraphDTO.getMetricEnd());
        dataChannelPO.setMetricStart(channelGraphDTO.getMetricStart());
        dataChannelPO.setMetricInterval(channelGraphDTO.getMetricInterval());
        dataChannelPO.setArrivalRate(channelGraphDTO.getArrivalRate());
        dataChannelPO.setEarlierRate(channelGraphDTO.getEarlierRate());
        dataChannelPO.setAlertThreshold(channelGraphDTO.getAlertThreshold());
        dataChannelPO.setIntervalType(channelGraphDTO.getIntervalType());
        return dataChannelPO;
    }

    public DataChannel toEntity(DataChannelDTO dataChannelDTO) {
        DataChannel dataChannel = new DataChannel();
        dataChannel.setId(dataChannelDTO.getChannelId());
        dataChannel.setName(dataChannelDTO.getName());
        dataChannel.setBizLine(dataChannelDTO.getBizLine());
        dataChannel.setDescription(dataChannelDTO.getDescription());
        dataChannel.setMetric(dataChannelDTO.getMetric());
        dataChannel.setMetricDayType(dataChannelDTO.getMetricDayType());
        dataChannel.setMetricStart(dataChannelDTO.getMetricStart());
        dataChannel.setMetricEnd(dataChannelDTO.getMetricEnd());
        dataChannel.setMetricInterval(dataChannelDTO.getMetricInterval());
        dataChannel.setIntervalType(dataChannelDTO.getIntervalType());
        dataChannel.setArrivalRate(dataChannelDTO.getArrivalRate());
        dataChannel.setEarlierRate(dataChannelDTO.getEarlierRate());
        dataChannel.setAlertThreshold(dataChannelDTO.getAlertThreshold());
        dataChannel.setIntegrityCheckFlag(dataChannelDTO.getIntegrityCheckFlag());
        dataChannel.setDataFormat(dataChannelDTO.getDataFormat());
        List<ChannelNodePO> nodes = dataChannelDTO.getNodes().stream().map(e -> channelGraphAssembler.toPO(e)).collect(Collectors.toList());
        dataChannel.setNodes(nodes);
        if (Objects.isNull(dataChannelDTO.getId())) {
            dataChannel.setCreator(dataChannelDTO.getCurrentUser());
            dataChannel.setCreateTime(new Date());
            dataChannel.setUpdater(dataChannelDTO.getCurrentUser());
            dataChannel.setUpdateTime(new Date());
        } else {
            dataChannel.setUpdater(dataChannelDTO.getCurrentUser());
            dataChannel.setUpdateTime(new Date());
        }
        return dataChannel;
    }

}
