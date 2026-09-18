package com.hexin.cbas.spider.plugin.flink;

import com.hexin.cbas.spider.core.node.Node;
import com.hexin.cbas.spider.core.node.NodeFactory;
import com.hexin.cbas.spider.core.pojo.MetricElement;

/**
 * @description:
 * @author: huangjiarong
 * @create: 2021/12/24
 * @version: v1.0
 */
public class FlinkNodeFactory  implements NodeFactory {

    private static final String FLINK = "flink";

    @Override
    public String type() {
        return FLINK;
    }

    @Override
    public Node buildNode(MetricElement element) {
        return null;
    }
}
