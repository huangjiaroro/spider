package com.hexin.cbas.spider.core.pojo;

import lombok.ToString;

/**
 * @description:
 * @author: huangjiarong
 * @create: 2021/9/29
 * @version: v1.0
 */
@ToString
public class MetricMeta {

    private static final int LAG = 1;
    private static final int OFFLINE_IN = 2;
    private static final int OFFLINE_OUT = 3;
    private static final int DEAL = 4;

    /**
     * 监控key
     */
    private String metricKey;
    /**
     * key描述
     */
    private String metricValue;
    /**
     * 占位符替换字段
     */
    private String linkColumn;

    private Integer targetType;

    private String targetUnit;

    public String getTargetUnit() {
        return targetUnit;
    }

    public void setTargetUnit(String targetUnit) {
        this.targetUnit = targetUnit;
    }

    public boolean lagTarget() {
        return LAG == targetType;
    }

    public boolean offlineInTarget() {
        return OFFLINE_IN == targetType;
    }

    public boolean offlineOutTarget() {
        return OFFLINE_OUT == targetType;
    }

    public boolean dealTarget() {
        return DEAL == targetType;
    }

    public Integer getTargetType() {
        return targetType;
    }

    public void setTargetType(Integer targetType) {
        this.targetType = targetType;
    }

    public String getMetricKey() {
        return metricKey;
    }

    public void setMetricKey(String metricKey) {
        this.metricKey = metricKey;
    }

    public String getMetricValue() {
        return metricValue;
    }

    public void setMetricValue(String metricValue) {
        this.metricValue = metricValue;
    }

    public String getLinkColumn() {
        return linkColumn;
    }

    public void setLinkColumn(String linkColumn) {
        this.linkColumn = linkColumn;
    }
}
