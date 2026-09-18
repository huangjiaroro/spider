package com.hexin.cbas.spider.application.schedule;

import com.hexin.cbas.spider.application.service.AlertRepresentationService;
import com.hexin.cbas.spider.application.service.MetricRecordApplicationService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @description: 通过告警表数据进行告警
 * @author: huangjiarong
 * @create: 2021/10/20
 * @version: v1.0
 */
@Service
public class DoAlertService implements InitializingBean {
    private static final ScheduledExecutorService SCHEDULED_ALERT_SERVICE = Executors.newScheduledThreadPool(1);
    public static final int SAVE_DAY = 3;


    private final AlertRepresentationService alertRepresentationService;


    private final MetricRecordApplicationService metricRecordApplicationService;

    public DoAlertService(AlertRepresentationService alertRepresentationService
            , MetricRecordApplicationService metricRecordApplicationService) {
        this.alertRepresentationService = alertRepresentationService;
        this.metricRecordApplicationService = metricRecordApplicationService;
    }

    /**
     * 开始执行告警
     */
    private void startDoAlert() {
        SCHEDULED_ALERT_SERVICE.scheduleWithFixedDelay(alertRepresentationService::alert, 0, 2, TimeUnit.MINUTES);
    }

    /**
     * 定时清理已告警数据
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void clearAlertedRecord() {
        alertRepresentationService.clearAlertedRecord();
    }


    /**
     * 定时清理监控数据
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void clearMetricRecord() {
        metricRecordApplicationService.clearMetricRecord(SAVE_DAY);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        startDoAlert();
    }


}
