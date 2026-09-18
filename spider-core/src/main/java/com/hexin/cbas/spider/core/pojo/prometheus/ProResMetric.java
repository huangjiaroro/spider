package com.hexin.cbas.spider.core.pojo.prometheus;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @description:
 * @author: huangjiarong
 * @create: 2021/9/29
 * @version: v1.0
 */
@ToString
@Data
public class ProResMetric {
    private ProResMetricDetail metric;
    private List<Object> value;

    /**
     * 获取实例
     *
     */
    public String getInstance() {
        return metric.getInstance();
    }

    /**
     * 获取指标值
     * list 第一个值为时间戳，第二个值为指标值
     *
     * @return
     */
    public Double getValue() {
        return Double.valueOf(value.get(1).toString());
    }


}
