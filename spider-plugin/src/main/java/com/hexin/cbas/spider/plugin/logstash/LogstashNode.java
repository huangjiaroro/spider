package com.hexin.cbas.spider.plugin.logstash;

import com.hexin.cbas.spider.core.client.IClient;
import com.hexin.cbas.spider.core.node.AbstractNode;
import com.hexin.cbas.spider.core.pojo.IResponse;
import com.hexin.cbas.spider.core.pojo.MetricElement;
import com.hexin.cbas.spider.core.pojo.prometheus.PrometheusResponse;
import com.hexin.cbas.spider.utils.JsonUtil;

import java.util.Date;

/**
 * @description:
 * @author: huangjiarong
 * @create: 2021/10/19
 * @version: v1.0
 */
public class LogstashNode extends AbstractNode {

    public LogstashNode(IClient client, MetricElement element) {
        super(client, element);
    }

    @Override
    protected IResponse doMetric(String metricKey, Date metricDate) {
        return JsonUtil.parseObject(getClient().doMetric(metricKey, metricDate), PrometheusResponse.class);
    }
}
