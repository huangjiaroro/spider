package com.hexin.cbas.spider.application.service;

import com.hexin.cbas.spider.core.loader.PluginLoader;
import com.hexin.cbas.spider.core.pojo.MetricElement;
import com.hexin.cbas.spider.core.pojo.MetricResult;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @description:
 * @author: huangjiarong
 * @create: 2021/10/11
 * @version: v1.0
 */
@Service
public class SpiderMetricService {

    /**
     * 获取监控数据
     * @param element
     * @param nodeType
     * @return
     */
    public MetricResult spiderMetric(MetricElement element, String nodeType) {
        return PluginLoader.loadFactory(nodeType)
                .buildNode(element)
                .metric();
    }

    public MetricResult spiderMetric(MetricElement element, String nodeType, Date metricDate) {
        return PluginLoader.loadFactory(nodeType)
                .buildNode(element)
                .metric(metricDate);
    }
}
