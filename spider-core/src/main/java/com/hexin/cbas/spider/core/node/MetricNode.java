package com.hexin.cbas.spider.core.node;

import com.hexin.cbas.spider.core.pojo.MetricResult;

import java.util.Date;

/**
 * @description:
 * @author: huangjiarong
 * @create: 2022/1/4
 * @version: v1.0
 */
public interface MetricNode {

    /**
     * 获取监控信息
     *
     * @return
     */
    MetricResult metric();

    /**
     * 获取指定日期监控信息
     *
     * @param metricDate
     * @return
     */
    MetricResult metric(Date metricDate);

}
