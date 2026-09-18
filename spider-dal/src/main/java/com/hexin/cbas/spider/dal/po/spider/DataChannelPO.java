package com.hexin.cbas.spider.dal.po.spider;

import lombok.Data;

import java.time.temporal.ChronoUnit;
import java.util.Date;

@Data
public class DataChannelPO {

    private static final Integer BUSINESS = 1;
    public static final Integer METRIC_START = 1;
    private static final Integer INTEGRITY_CHECK = 1;

    private Long id;

    private String name;

    private String state;

    private Integer integrityCheckFlag;

    private String description;

    private String bizLine;

    private String keyword;

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

    private Integer alertThreshold;

    private String arrivalRate;

    private String earlierRate;

    private Double lagTime;

    private Boolean health;

    private String dataFormat;

    public boolean businessMetric() {
        return BUSINESS.equals(metricDayType);
    }

    public boolean integrityCheck() {
        return INTEGRITY_CHECK.equals(integrityCheckFlag);
    }

    public ChronoUnit toChronoUnit() {
        switch (intervalType) {
            case 0:
                return ChronoUnit.SECONDS;
            case 1:
                return ChronoUnit.MINUTES;
            case 2:
                return ChronoUnit.HOURS;
            default:
                return ChronoUnit.SECONDS;
        }

    }

}