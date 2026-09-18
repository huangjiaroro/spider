package com.hexin.cbas.spider.core.client;


import com.hexin.cbas.spider.config.SpiderConfig;
import com.hexin.cbas.spider.utils.HttpUtil;

import java.net.URLEncoder;
import java.util.Date;
import java.util.Objects;

/**
 * @description: 从prometheus获取指标数据
 * @author: huangjiarong
 * @create: 2021/9/15
 * @version: v1.0
 */
public class PrometheusClient implements IClient {


    private static final String PROMETHEUS_API = SpiderConfig.PROMETHEUS_ADDRESS + "?query=%s";

    @Override
    public String doMetric(String metricKey, Date metricTime) {

        try {
            String url = String.format(PROMETHEUS_API, URLEncoder.encode(metricKey, "utf-8"));
            //实时指标不带时间参数
            if (Objects.isNull(metricTime)) {
                return HttpUtil.httpGet(url);
            } else {
                return HttpUtil.httpGet(url + "&time=" + metricTime.getTime() / 1000);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}


