package com.hexin.cbas.spider.plugin.kafka;

import com.hexin.cbas.spider.core.client.PrometheusClient;
import com.hexin.cbas.spider.core.node.Node;
import com.hexin.cbas.spider.core.node.NodeFactory;
import com.hexin.cbas.spider.core.pojo.MetricElement;

/**
 * @description:
 * @author: huangjiarong
 * @create: 2021/9/15
 * @version: v1.0
 */
public class KafkaNodeFactory implements NodeFactory {

    public static final String KAFKA = "kafka";

    @Override
    public String type() {
        return KAFKA;
    }

    @Override
    public Node buildNode(MetricElement element) {
        return new KafkaNode(new PrometheusClient(), element);
    }
}
