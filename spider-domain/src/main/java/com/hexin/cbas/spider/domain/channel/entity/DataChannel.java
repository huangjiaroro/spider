package com.hexin.cbas.spider.domain.channel.entity;

import com.hexin.cbas.spider.dal.po.spider.ChannelNodePO;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Data
@Getter
@Setter
public class DataChannel {
    private Long id;

    private String name;

    private String bizLine;

    private String state;

    private Integer integrityCheckFlag;

    private String description;

    private String creator;

    private Date createTime;

    private String updater;

    private Date updateTime;

    private Integer metric;

    private Integer metricDayType;

    private String metricStart;

    private String metricEnd;

    private Integer metricInterval;

    private Integer intervalType;

    private String arrivalRate;

    private String earlierRate;

    private Integer alertThreshold;

    private Double lagTime;

    private Boolean health;

    private String dataFormat;

    private List<ChannelNodePO> nodes;
}