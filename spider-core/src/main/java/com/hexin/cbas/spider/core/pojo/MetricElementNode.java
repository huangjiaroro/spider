package com.hexin.cbas.spider.core.pojo;


import com.hexin.cbas.spider.enums.OperatorEnum;
import lombok.ToString;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * @description: 监控项配置
 * @author: huangjiarong
 * @create: 2021/9/29
 * @version: v1.0
 */
@ToString
public class MetricElementNode {
    /**
     * metric_meta
     */
    @JsonIgnore
    private MetricMeta metricMeta;

    /**
     * metric_meta id
     */
    private int metricId;

    /**
     * 操作符
     */
    private OperatorEnum operator;
    /**
     * 告警阈值
     */
    private int alertValue;

    /**
     * 是否通过instance详情来告警
     */
    private boolean detailAlert;

    public int getMetricId() {
        return metricId;
    }

    public void setMetricId(int metricId) {
        this.metricId = metricId;
    }

    public MetricMeta getMetricMeta() {
        return metricMeta;
    }

    public void setMetricMeta(MetricMeta metricMeta) {
        this.metricMeta = metricMeta;
    }

    public int getAlertValue() {
        return alertValue;
    }

    public void setAlertValue(int alertValue) {
        this.alertValue = alertValue;
    }

    public OperatorEnum getOperator() {
        return operator;
    }

    public void setOperator(OperatorEnum operator) {
        this.operator = operator;
    }

    public boolean isDetailAlert() {
        return detailAlert;
    }

    public void setDetailAlert(boolean detailAlert) {
        this.detailAlert = detailAlert;
    }
}
