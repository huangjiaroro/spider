package com.hexin.cbas.spider.dal.po.spider;

import lombok.Data;

import java.util.Date;
/**
 * 实时指标监控
 * @author huangjiarong@myhexin.com
 * @date 2023/9/22 15:27
 **/
@Data
public class MetricRecordPO {
    private Integer id;

    private Integer channelId;

    private String channelName;

    private Integer lag;

    private Double lagTime;

    private Boolean health;

    private Date metricTime;

    /**
     * 链路吞吐
     **/
    private Double dealRate;

    private String metricDate;
}