package com.hexin.cbas.spider.application.dto;

import lombok.Data;

import java.util.List;

@Data
public class DataChannelDetailDTO {
    private Long id;

    private String name;

    private String bizLine;

    private String state;

    private String description;

    private String dataFormat;

    /**
     * 链路是否健康
     */
    private Boolean health;

    /**
     * 链路延迟
     */
    private Double lag;

    /**
     * 链路延迟
     */
    private Double lagTime;

    /**
     * 节点列表
     */
    private List<ChannelNodeMetricDTO> nodes;

    /**
     * 边集合
     */
    private List<EdgeDTO> edges;

}