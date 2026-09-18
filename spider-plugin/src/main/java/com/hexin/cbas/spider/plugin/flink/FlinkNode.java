package com.hexin.cbas.spider.plugin.flink;

import com.hexin.cbas.spider.core.client.IClient;
import com.hexin.cbas.spider.core.node.AbstractNode;
import com.hexin.cbas.spider.core.pojo.IResponse;
import com.hexin.cbas.spider.core.pojo.MetricElement;

import java.util.Date;

/**
 * @description:
 * @author: huangjiarong
 * @create: 2021/12/24
 * @version: v1.0
 */
public class FlinkNode extends AbstractNode {

    public FlinkNode(IClient client, MetricElement element) {
        super(client, element);
    }

    @Override
    protected IResponse doMetric(String metricKey, Date metricDate) {
        return null;
    }
}
