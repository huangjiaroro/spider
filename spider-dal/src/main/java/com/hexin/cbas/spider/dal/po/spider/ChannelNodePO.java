package com.hexin.cbas.spider.dal.po.spider;

import com.hexin.cbas.spider.core.pojo.MetricElement;
import lombok.Data;

/**
 * 通道节点
 *
 * @author huangjiarong
 */
@Data
public class ChannelNodePO extends MetricElement {
    private Integer id;

    private String workTree;

    /**
     * 节点基础信息
     */
    String metricInfo;

    private String nodeType;

    private Integer alert;

    private String metricElement;

    private String nodeId;

    private String parentId;

    private Integer channelId;

    private String drawJson;

}