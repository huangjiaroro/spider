package com.hexin.cbas.spider.application.service;

import com.hexin.cbas.spider.application.assembler.DataChannelAssembler;
import com.hexin.cbas.spider.application.assembler.DataChannelGraphAssembler;
import com.hexin.cbas.spider.application.dto.*;
import com.hexin.cbas.spider.core.pojo.MetricMeta;
import com.hexin.cbas.spider.dal.mapper.spider.ChannelNodeMapper;
import com.hexin.cbas.spider.dal.mapper.spider.DataChannelMapper;
import com.hexin.cbas.spider.dal.mapper.spider.InfoMetaMapper;
import com.hexin.cbas.spider.dal.mapper.spider.MetricMetaMapper;
import com.hexin.cbas.spider.dal.po.spider.ChannelNodePO;
import com.hexin.cbas.spider.dal.po.spider.MetricMetaPO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: huangjiarong
 * @create: 2021/10/13
 * @version: v1.0
 */
@Service
public class DataGraphApplicationService {

    @Autowired
    DataChannelAssembler channelAssembler;

    @Autowired
    DataChannelGraphAssembler channelGraphAssembler;

    @Autowired
    InfoMetaMapper infoMetaMapper;

    @Autowired
    MetricMetaMapper metricMetaMapper;

    @Autowired
    ChannelNodeMapper channelNodeMapper;

    @Autowired
    DataChannelMapper channelMapper;

    public List<InfoMetaDTO> getNodeInfo(String nodeType, Integer classify) {
        return infoMetaMapper.selectByTypeAndClassify(nodeType, classify).stream()
                .map(p -> channelGraphAssembler.toInfoMetaDTO(p))
                .collect(Collectors.toList());
    }

    public List<KeyValueDTO> getNodeMetricInfo(String nodeType) {
        return metricMetaMapper.selectByType(nodeType).stream()
                .map(p -> channelGraphAssembler.toDTO(p))
                .collect(Collectors.toList());
    }

    public List<MetricMetaPO> nodeOfflineInTarget(String nodeType) {
        return metricMetaMapper.selectByType(nodeType).stream().filter(MetricMeta::offlineInTarget).collect(Collectors.toList());
    }

    public List<MetricMetaPO> nodeOfflineOutTarget(String nodeType) {
        return metricMetaMapper.selectByType(nodeType).stream().filter(MetricMeta::offlineOutTarget).collect(Collectors.toList());
    }


    @Transactional(rollbackFor = Exception.class)
    public void save(ChannelGraphDTO channelGraphDTO) {

        //更新channel
        channelMapper.updateByPrimaryKeySelective(channelAssembler.toPO(channelGraphDTO));

        //保存时进行节点的校验删除和添加
        List<Integer> oldIds = channelNodeMapper.selectByChannelId(channelGraphDTO.getChannelId()).stream()
                .map(ChannelNodePO::getId).collect(Collectors.toList());
        List<Integer> newIds = channelGraphDTO.getNodes().stream().map(ChannelNodeDTO::getNodeId).collect(Collectors.toList());
        //对不存在的点进行删除
        oldIds.removeAll(newIds);
        oldIds.forEach(channelNodeMapper::deleteByPrimaryKey);
        channelGraphDTO.getNodes().forEach(p -> {
            if (p.getNodeId() == null) {
                channelNodeMapper.insertSelective(channelGraphAssembler.toPO(p));
            } else {
                channelNodeMapper.updateByPrimaryKeySelective(channelGraphAssembler.toPO(p));
            }
        });
    }

    public ChannelGraphDTO get(Integer channelId) {
        ChannelGraphDTO graph = channelAssembler.toDTO(channelMapper.selectByPrimaryKey(channelId.longValue()));
        List<ChannelNodePO> nodes = channelNodeMapper.selectByChannelId(channelId);
        //组装图
        graph.setNodes(nodes.stream()
                .map(p -> channelGraphAssembler.toDTO(p))
                .collect(Collectors.toList()));
        //获取首节点
        List<EdgeDTO> edges = new ArrayList<>();
        graph.setEdges(edges);
        List<ChannelNodeDTO> roots = graph.getNodes().stream().filter(p -> StringUtils.isEmpty(p.getParentId())).collect(Collectors.toList());
        graph.setRoots(roots);
        Queue<ChannelNodeDTO> queue = new LinkedList<>();
        queue.addAll(roots);
        while (!queue.isEmpty()) {
            ChannelNodeDTO node = queue.poll();
            List<ChannelNodeDTO> targets = graph.getNodes().stream().filter(p -> node.getId().equals(p.getParentId())).collect(Collectors.toList());
            for (ChannelNodeDTO target : targets) {
                edges.add(new EdgeDTO(node.getId(), node, target.getId(), target));
                queue.add(target);
            }
        }
        return graph;
    }

    /**
     * 构建只有执行节点的新图
     *
     * @param graph
     */
    public void buildExecuteNodeGraph(ChannelGraphDTO graph) {

        //删除node的虚拟节点
        graph.setNodes(graph.getNodes().stream().filter(ChannelNodeDTO::executeNode).collect(Collectors.toList()));

        if (graph.getNodes().size() <= 1) {
            graph.setSinks(graph.getNodes());
            return;
        }

        //设置source 和sink
        List<EdgeDTO> edges = graph.getEdges();
        List<ChannelNodeDTO> sources = edges.stream().map(EdgeDTO::getSourceNode).filter(ChannelNodeDTO::executeNode).collect(Collectors.toList());
        List<ChannelNodeDTO> targets = edges.stream().map(EdgeDTO::getTargetNode).filter(ChannelNodeDTO::executeNode).collect(Collectors.toList());
        for (EdgeDTO edge : edges) {
            if (!edge.getSourceNode().executeNode()) {
                targets.remove(edge.getTargetNode());
            }
            if (!edge.getTargetNode().executeNode()) {
                sources.remove(edge.getSourceNode());
            }
        }
        List<ChannelNodeDTO> roots = new ArrayList<>(sources);
        roots.removeAll(targets);
        graph.setRoots(roots);
        targets.removeAll(sources);
        graph.setSinks(targets);
    }


    public List<String> selectBusinessChannel(List<String> businessCodes) {
        return channelNodeMapper.selectBusinessChannel(businessCodes);
    }
}
