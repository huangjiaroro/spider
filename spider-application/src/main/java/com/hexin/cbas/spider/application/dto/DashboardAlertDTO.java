package com.hexin.cbas.spider.application.dto;

import lombok.Data;

/**
 * @description:
 * @author: huangjiarong
 * @create: 2021/12/20
 * @version: v1.0
 */
@Data
public class DashboardAlertDTO {
    /**
     * 通道名称
     */
    private String channelName;
    /**
     * 告警开始时间
     */
    private String alertStart;
    /**
     * 告警结束时间
     */
    private String alertEnd;
    /**
     * 持续时间
     */
    private String alertDuration;
    /**
     * 命中告警次数
     */
    private Long hitTimes;

}
