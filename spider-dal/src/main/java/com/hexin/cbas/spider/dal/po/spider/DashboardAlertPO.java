package com.hexin.cbas.spider.dal.po.spider;

import com.hexin.cbas.spider.utils.DateUtil;
import lombok.Data;

import java.util.Date;

/**
 * @description:
 * @author: huangjiarong
 * @create: 2021/12/20
 * @version: v1.0
 */
@Data
public class DashboardAlertPO {

    /**
     * 通道名称
     */
    private String channelName;

    /**
     * 告警开始时间
     */
    private Date alertStart;

    /**
     * 告警结束时间
     */
    private Date alertEnd;

    /**
     * 持续时间
     */
    private String alertDuration;

    /**
     * 命中告警次数
     */
    private Long hitTimes;


    public DashboardAlertPO computeAlertDuration() {
        long minutes = (alertEnd.getTime() - alertStart.getTime()) / 1000 / 60;
        long durationHour = minutes / 60;
        long durationMinutes = minutes % 60;
        this.alertDuration = durationHour > 0 ? String.format("%sh%sm", durationHour, durationMinutes) : String.format("%sm", durationMinutes);
        return this;
    }

}
