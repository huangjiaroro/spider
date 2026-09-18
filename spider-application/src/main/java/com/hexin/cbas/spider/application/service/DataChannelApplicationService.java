package com.hexin.cbas.spider.application.service;

import com.hexin.cbas.spider.application.assembler.DataChannelAssembler;
import com.hexin.cbas.spider.application.dto.ChannelRealTimeMetricDTO;
import com.hexin.cbas.spider.application.dto.DataChannelDTO;
import com.hexin.cbas.spider.application.dto.DataChannelDetailDTO;
import com.hexin.cbas.spider.application.schedule.GraphMetricService;
import com.hexin.cbas.spider.core.pojo.MetricElementNode;
import com.hexin.cbas.spider.domain.channel.entity.DataChannel;
import com.hexin.cbas.spider.domain.channel.service.DataChannelDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author wanghujia
 * @since 2021/8/11
 */
@Service
@SuppressWarnings("all")
public class DataChannelApplicationService {

    @Autowired
    private DataChannelDomainService dataChannelDomainService;

    @Autowired
    private DataChannelAssembler dataChannelAssembler;

    @Autowired
    private GraphMetricService metricService;

    @Autowired
    private PathLifeCycleService pathLifeCycleService;

    @Transactional(rollbackFor = Exception.class)
    public void save(DataChannelDTO dataChannelDTO) {
        //判断节点是否是源端节点是的话,分支检测强行开启
        dataChannelDTO.getNodes().forEach(node -> {
            if (Boolean.TRUE.equals(node.getSourceNode())) {
                List<MetricElementNode> elementNodes = node.getElementNodes();
                if (!CollectionUtils.isEmpty(elementNodes)) {
                    elementNodes.forEach(elementNode -> elementNode.setDetailAlert(true));
                }
            }
        });
        DataChannel entity = dataChannelAssembler.toEntity(dataChannelDTO);
        dataChannelDomainService.save(entity);
        entity.getNodes().forEach(node -> {
            //如果是hdfs则需要操作hdfs路径生命周期表
            if ("hdfs".equals(node.getNodeType())) {
                pathLifeCycleService.savePath(node.getMetricInfos(), entity.getId(), dataChannelDTO.getCurrentUser()
                        , dataChannelDTO.getBizLine(), dataChannelDTO.getName());
            }
        });
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(DataChannelDTO dataChannelDTO) {
        dataChannelDomainService.delete(dataChannelDTO.getChannelId());
        pathLifeCycleService.deleteByChannelId(dataChannelDTO.getChannelId());
    }

    public DataChannelDetailDTO get(Long id) {
        DataChannelDetailDTO dataChannelDetailDTO = new DataChannelDetailDTO();
        DataChannel dataChannel = dataChannelDomainService.getById(id);
        // 获取链路实时监控信息
        ChannelRealTimeMetricDTO channelRealTimeMetricDTO = metricService.channelMetric(Math.toIntExact(id));
        // 组装返回信息
        dataChannelDetailDTO.setId(id);
        dataChannelDetailDTO.setName(dataChannel.getName());
        dataChannelDetailDTO.setBizLine(dataChannel.getBizLine());
        dataChannelDetailDTO.setDataFormat(dataChannel.getDataFormat());
        dataChannelDetailDTO.setState(dataChannel.getState());
        dataChannelDetailDTO.setDescription(dataChannel.getDescription());
        dataChannelDetailDTO.setHealth(channelRealTimeMetricDTO.getHealth());
        dataChannelDetailDTO.setLag(channelRealTimeMetricDTO.getLag());
        dataChannelDetailDTO.setLagTime(channelRealTimeMetricDTO.getLagTime());
        dataChannelDetailDTO.setNodes(channelRealTimeMetricDTO.getNodes());
        dataChannelDetailDTO.setEdges(channelRealTimeMetricDTO.getEdges());
        return dataChannelDetailDTO;
    }

    @Transactional(rollbackFor = Exception.class)
    public void changeState(DataChannelDTO dataChannelDTO) {
        dataChannelDomainService.changeState(dataChannelDTO.getId(), dataChannelDTO.getState());
    }

    public void changeMetric(DataChannelDTO dataChannelDTO) {
        dataChannelDomainService.changeMetric(dataChannelDTO.getId(), dataChannelDTO.getMetric());
    }
}
