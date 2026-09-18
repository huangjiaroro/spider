package com.hexin.cbas.spider.core.node;


import com.hexin.cbas.spider.core.client.IClient;
import com.hexin.cbas.spider.core.pojo.MetricElement;

/**
 * @description:执行的节点
 * @author: huangjiarong
 * @create: 2021/9/15
 * @version: v1.0
 */
public interface Node extends MetricNode, ExecuteNode {
    /**
     * 返回Client
     *
     * @return
     */
    IClient getClient();

    /**
     * 返回element
     *
     * @return
     */
    MetricElement getElement();
}
