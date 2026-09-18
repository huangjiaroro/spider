package com.hexin.cbas.spider.core.pojo;

import com.hexin.cbas.spider.config.HostKey;
import com.hexin.cbas.spider.constants.MarkConstant;
import lombok.Data;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

/**
 * @description: 监控结果组装数据
 * @author: huangjiarong
 * @create: 2021/9/30
 * @version: v1.0
 */
@ToString
@Data
public class MetricData {

    /**
     * 是否健康
     */
    private boolean isHealth = true;

    private String key;

    private String value;

    private String targetUnit;

    private String metricKey;

    private Map<HostKey, Object> detail = new HashMap<>();

    public static MetricData build(Map<HostKey, Double> metricDetail, Double metricValue, String key, String targetUnit, String metricKey) {
        MetricData data = new MetricData();
        data.setKey(key);
        data.setValue(String.format(MarkConstant.DOUBLE_FORMAT, metricValue));
        data.setTargetUnit(targetUnit);
        data.setMetricKey(metricKey);
        metricDetail.forEach((key1, value1) -> data.getDetail().put(key1, String.format(MarkConstant.DOUBLE_FORMAT, value1)));
        return data;
    }

    public static MetricData build(Double metricValue, String key, String targetUnit, String metricKey) {
        MetricData data = new MetricData();
        data.setKey(key);
        data.setValue(String.format(MarkConstant.DOUBLE_FORMAT, metricValue));
        data.setTargetUnit(targetUnit);
        data.setMetricKey(metricKey);
        return data;
    }

    public void metricDetail(Map<HostKey, Double> metricDetail) {
        metricDetail.forEach((key1, value1) -> this.detail.put(key1, String.format(MarkConstant.DOUBLE_FORMAT, value1)));
    }

}
