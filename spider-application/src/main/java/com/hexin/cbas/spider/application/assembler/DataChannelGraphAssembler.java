package com.hexin.cbas.spider.application.assembler;

import com.hexin.cbas.spider.application.dto.ChannelNodeDTO;
import com.hexin.cbas.spider.application.dto.InfoMetaDTO;
import com.hexin.cbas.spider.application.dto.KeyValueDTO;
import com.hexin.cbas.spider.application.schedule.pojo.ChannelScheduleNode;
import com.hexin.cbas.spider.dal.po.spider.ChannelNodePO;
import com.hexin.cbas.spider.dal.po.spider.InfoMetaPO;
import com.hexin.cbas.spider.dal.po.spider.MetricMetaPO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @description:
 * @author: huangjiarong
 * @create: 2021/10/13
 * @version: v1.0
 */
@Mapper(componentModel = "spring")
public interface DataChannelGraphAssembler {

    /**
     * 对象转换
     *
     * @param infoMetaPO
     * @return
     */
    @Mapping(source = "infoKey", target = "key")
    @Mapping(source = "infoValue", target = "value")
    KeyValueDTO toDTO(InfoMetaPO infoMetaPO);

    /**
     * 对象转换
     *
     * @param metricMetaPO
     * @return
     */
    @Mapping(source = "id", target = "key")
    @Mapping(source = "metricValue", target = "value")
    KeyValueDTO toDTO(MetricMetaPO metricMetaPO);

    /**
     * 对象转换
     *
     * @param channelNodePO
     * @return
     */
    @Mapping(target = "elementNodes", expression = "java(com.hexin.cbas.spider.utils.JsonUtil.parseObject(channelNodePO.getMetricElement(), new org.codehaus.jackson.type.TypeReference<List<MetricElementNode>>(){}))")
    @Mapping(target = "metricInfos", expression = "java(com.hexin.cbas.spider.utils.JsonUtil.parseObject(channelNodePO.getMetricInfo(), Map.class))")
    @Mapping(target = "drawJson", expression = "java(com.hexin.cbas.spider.utils.JsonUtil.parseObject(channelNodePO.getDrawJson(), Map.class))")
    @Mapping(target = "name", source = "nodeName")
    @Mapping(target = "id", source = "nodeId")
    @Mapping(target = "nodeId", source = "id")
    ChannelNodeDTO toDTO(ChannelNodePO channelNodePO);


    @Mapping(target = "metricElement", expression = "java(com.hexin.cbas.spider.utils.JsonUtil.parseString(channelNodeDTO.getElementNodes()))")
    @Mapping(target = "metricInfo", expression = "java(com.hexin.cbas.spider.utils.JsonUtil.parseString(channelNodeDTO.getMetricInfos()))")
    @Mapping(target = "drawJson", expression = "java(com.hexin.cbas.spider.utils.JsonUtil.parseString(channelNodeDTO.getDrawJson()))")
    @Mapping(target = "nodeName", source = "name")
    @Mapping(target = "nodeId", source = "id")
    @Mapping(target = "id", source = "nodeId")
    ChannelNodePO toPO(ChannelNodeDTO channelNodeDTO);


    /**
     * 对象转换
     *
     * @param channelNodePO
     * @return
     */
    @Mapping(target = "elementNodes", expression = "java(com.hexin.cbas.spider.utils.JsonUtil.parseObject(channelNodePO.getMetricElement(), new org.codehaus.jackson.type.TypeReference<List<MetricElementNode>>(){}))")
    @Mapping(target = "metricInfos", expression = "java(com.hexin.cbas.spider.utils.JsonUtil.parseObject(channelNodePO.getMetricInfo(), Map.class))")
    @Mapping(target = "drawJson", expression = "java(com.hexin.cbas.spider.utils.JsonUtil.parseObject(channelNodePO.getDrawJson(), Map.class))")
    @Mapping(target = "name", source = "nodeName")
    @Mapping(target = "id", source = "nodeId")
    @Mapping(target = "nodeId", source = "id")
    ChannelScheduleNode toEntity(ChannelNodePO channelNodePO);

    /**
     * 对象转换
     *
     * @param infoMetaPO
     * @return
     */
    @Mapping(source = "infoKey", target = "key")
    @Mapping(source = "infoValue", target = "value")
    InfoMetaDTO toInfoMetaDTO(InfoMetaPO infoMetaPO);
}
