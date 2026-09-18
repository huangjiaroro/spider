package com.hexin.cbas.spider.plugin.flume;

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
public class FlumeNodeFactory implements NodeFactory {

    public static final String FLUME = "flume";

    @Override
    public String type() {
        return FLUME;
    }

    @Override
    public Node buildNode(MetricElement element) {
        return new FlumeNode(new PrometheusClient(), element);
    }
}
