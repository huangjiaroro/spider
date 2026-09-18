package com.hexin.cbas.spider.plugin.logstash;

import com.hexin.cbas.spider.core.client.PrometheusClient;
import com.hexin.cbas.spider.core.node.Node;
import com.hexin.cbas.spider.core.node.NodeFactory;
import com.hexin.cbas.spider.core.pojo.MetricElement;

/**
 * @description:
 * @author: huangjiarong
 * @create: 2021/10/19
 * @version: v1.0
 */
public class LogstashNodeFactory implements NodeFactory {

    public static final String LOGSTASH = "logstash";

    @Override
    public String type() {
        return LOGSTASH;
    }

    @Override
    public Node buildNode(MetricElement element) {
        return new LogstashNode(new PrometheusClient(), element);
    }
}
