package com.hexin.cbas.spider.core.pojo.prometheus;

import lombok.ToString;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: huangjiarong
 * @create: 2021/9/29
 * @version: v1.0
 */
@ToString
public class ProResData {
    List<ProResMetric> result;

    /**
     * 获取服务器实例列表
     *
     * @return
     */
    public Set<String> getInstances() {
        return result.stream().map(ProResMetric::getInstance).collect(Collectors.toSet());
    }

    /**
     * 获取指标值
     *
     * @return
     */
    public Double getMetricValue() {
        return result
                .stream()
                .mapToDouble(ProResMetric::getValue)
                .sum();
    }

    /**
     * 根据instance分组获取值
     *
     * @return
     */
    public Map<String, Double> getMetricValueGroup() {
        Set<String> instances = getInstances();
        Map<String, Double> group = new HashMap<>();
        for (String instance : instances) {
            group.put(instance, result.stream()
                    .filter(value -> value.getInstance().equals(instance))
                    .mapToDouble(ProResMetric::getValue)
                    .sum());
        }
        return group;
    }


    public List<ProResMetric> getResult() {
        return result;
    }

    public void setResult(List<ProResMetric> result) {
        this.result = result;
    }
}
