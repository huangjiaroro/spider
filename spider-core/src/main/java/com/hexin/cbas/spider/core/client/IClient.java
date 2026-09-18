package com.hexin.cbas.spider.core.client;

import java.util.Date;

/**
 * @description:
 * @author: huangjiarong
 * @create: 2021/9/15
 * @version: v1.0
 */
public interface IClient {

    /**
     * 获取指标数据
     *
     * @param metricKey  指标key
     * @param metricTime 指标时间
     * @return
     */
    String doMetric(String metricKey, Date metricTime);


}
