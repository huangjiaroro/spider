package com.hexin.cbas.spider.application.dto;

import lombok.Data;

import java.util.List;

/**
 * 数据通道实体类
 *
 * @author wanghujia
 * @since 2022/1/6
 */
@Data
public class DataChannelDTO {

    private Long id;

    private Long channelId;

    private String name;

    private String bizLine;

    private String description;

    private String currentUser;

    private Integer metric;

    private Integer metricDayType;

    private String metricStart;

    private String metricEnd;

    private Integer metricInterval;

    private Integer intervalType;

    private String arrivalRate;

    private String earlierRate;

    private Integer alertThreshold;

    private Integer integrityCheckFlag;

    private List<ChannelNodeDTO> nodes;

    private String state;

    private Boolean health;

    private String lagTime;

    private String dataFormat;
}