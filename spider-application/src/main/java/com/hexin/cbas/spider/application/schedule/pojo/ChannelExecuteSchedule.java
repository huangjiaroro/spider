package com.hexin.cbas.spider.application.schedule.pojo;

import com.hexin.cbas.spider.dal.po.spider.DataChannelPO;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * @description: 通道执行计划
 * @author: huangjiarong
 * @create: 2021/10/14
 * @version: v1.0
 */
@Data
public class ChannelExecuteSchedule {
    private int singleAlertMatchTimes;

    private DataChannelPO channel;

    private Integer metricInterval;

    private ChronoUnit unit;

    private LocalTime startTime;

    private LocalTime endTime;

    private boolean isBusiness;

    /**
     * 下次执行时间
     */
    private LocalDateTime nextExecuteTime;

    /**
     * 执行节点列表
     */
    private List<ChannelScheduleNode> nodes;

    public void increaseAlert() {
        singleAlertMatchTimes += 1;
    }

    public void resizeAlert() {
        singleAlertMatchTimes = 0;
    }

}
