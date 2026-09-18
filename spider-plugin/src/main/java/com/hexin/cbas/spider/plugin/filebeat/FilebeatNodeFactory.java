package com.hexin.cbas.spider.plugin.filebeat;

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
public class FilebeatNodeFactory implements NodeFactory {

    public static final String FILE_BEAT = "filebeat";

    @Override
    public String type() {
        return FILE_BEAT;
    }

    @Override
    public Node buildNode(MetricElement element) {
        return new FilebeatNode(new PrometheusClient(), element);
    }

}
