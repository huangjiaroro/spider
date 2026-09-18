package com.hexin.cbas.spider.application.dto;

import com.hexin.cbas.spider.core.pojo.MetricData;
import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: huangjiarong
 * @create: 2021/10/14
 * @version: v1.0
 */
@Data
public class ChannelNodeMetricDTO {
    private String id;

    private String parentId;

    private Integer channelId;

    private Integer nodeId;

    private String nodeType;

    private String nodeName;

    private Integer classify;

    /**
     * 链路是否健康
     */
    private Boolean health;

    private List<MetricData> headers;

    private List<List<Object>> rows;
}
