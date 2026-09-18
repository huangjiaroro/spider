package com.hexin.cbas.spider.application.dto;

import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: huangjiarong
 * @create: 2021/10/13
 * @version: v1.0
 */
@Data
public class ChannelGraphDTO {

    private Integer channelId;

    private String name;

    private String bizLine;

    private String description;

    private String dataFormat;

    private Integer metric;

    private Integer metricDayType;

    private String metricStart;

    private String metricEnd;

    private Integer metricInterval;

    private Integer alertThreshold;

    private String arrivalRate;

    private String earlierRate;

    private Integer intervalType;

    private Integer integrityCheckFlag;

    List<EdgeDTO> edges;

    List<ChannelNodeDTO> roots;

    List<ChannelNodeDTO> nodes;

    List<ChannelNodeDTO> sinks;

}
