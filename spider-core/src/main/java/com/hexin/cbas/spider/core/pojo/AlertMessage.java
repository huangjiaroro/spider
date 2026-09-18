package com.hexin.cbas.spider.core.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

/**
 * @description:
 * @author: huangjiarong
 * @create: 2021/9/30
 * @version: v1.0
 */
@ToString
@EqualsAndHashCode(of = {"alertContent", "alertPerson", "channelName"})
@Data
public class AlertMessage {


    private String metricKey;

    /**
     * 告警内容
     */
    private String alertContent;
    /**
     * 告警级别
     */
    private Integer alertLevel;
    /**
     * 告警人
     */
    private String alertPerson;

    /**
     * 告警时间
     */
    private Date alertTime;

    /**
     * 通道名称
     */
    private String channelName;

    /**
     * paas告警实例
     */
    private String paasAlertInstance;

    public AlertMessage() {
    }


    public AlertMessage(String alertContent, Integer alertLevel, String alertPerson, String metricKey
            , String channelName, String paasAlertInstance) {
        this.alertContent = alertContent;
        this.alertLevel = alertLevel;
        this.alertPerson = alertPerson;
        this.metricKey = metricKey;
        this.alertTime = new Date();
        this.channelName = channelName;
        this.paasAlertInstance = paasAlertInstance;
    }
}
