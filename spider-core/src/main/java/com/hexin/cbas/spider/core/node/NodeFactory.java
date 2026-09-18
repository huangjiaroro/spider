package com.hexin.cbas.spider.core.node;


import com.hexin.cbas.spider.core.pojo.MetricElement;

/**
 * @description:获取MetricNode工厂类
 * @author: huangjiarong
 * @create: 2021/9/15
 * @version: v1.0
 */
public interface NodeFactory {

    /**
     * metric类型
     *
     * @return
     */
    String type();

    /**
     * 创建metric node
     *
     * @param element
     * @return
     */
    Node buildNode(MetricElement element);
}
