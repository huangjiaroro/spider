package com.hexin.cbas.spider.core.pojo;


import lombok.ToString;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: huangjiarong
 * @create: 2021/9/29
 * @version: v1.0
 */
@ToString
public class MetricElement {

    private static final int VIRTUAL_SOURCE_NODE = 1;
    private static final int EXECUTE_NODE = 2;
    private static final int VIRTUAL_SINK_NODE = 3;
    /**
     * 监控项信息
     */
    List<MetricElementNode> elementNodes;

    /**
     * 节点基础信息
     */
    Map<String, Object> metricInfos;
    /**
     * 节点名称
     */
    private String nodeName;
    /**
     * 告警人
     */
    private String alertPerson;
    /**
     * 告警等级
     */
    private Integer alertLevel;
    /**
     * 通道名称
     */
    private String channelName;

    /**
     * 节点类型
     */
    private Integer classify;

    /**
     * 业务树信息
     */
    private String workTree;

    /**
     * 是否是源端节点
     */
    private Boolean sourceNode;


    public boolean virtualSourceNode() {
        return VIRTUAL_SOURCE_NODE == classify;
    }

    public boolean virtualSinkNode() {
        return VIRTUAL_SINK_NODE == classify;
    }

    public boolean executeNode() {
        return EXECUTE_NODE == classify;
    }

    public Integer getClassify() {
        return classify;
    }

    public void setClassify(Integer classify) {
        this.classify = classify;
    }

    public List<MetricElementNode> getElementNodes() {
        return elementNodes;
    }

    public Map<String, Object> getMetricInfos() {
        return metricInfos;
    }

    public void setMetricInfos(Map<String, Object> metricInfos) {
        this.metricInfos = metricInfos;
    }

    public void setElementNodes(List<MetricElementNode> elementNodes) {
        this.elementNodes = elementNodes;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getAlertPerson() {
        return alertPerson;
    }

    public void setAlertPerson(String alertPerson) {
        this.alertPerson = alertPerson;
    }

    public Integer getAlertLevel() {
        return alertLevel;
    }

    public void setAlertLevel(Integer alertLevel) {
        this.alertLevel = alertLevel;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getWorkTree() {
        return workTree;
    }

    public void setWorkTree(String workTree) {
        this.workTree = workTree;
    }

    public Boolean getSourceNode() {
        return sourceNode;
    }

    public void setSourceNode(Boolean sourceNode) {
        this.sourceNode = sourceNode;
    }
}
