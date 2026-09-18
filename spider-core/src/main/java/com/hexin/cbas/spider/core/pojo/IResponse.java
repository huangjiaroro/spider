package com.hexin.cbas.spider.core.pojo;

import java.util.Map;
import java.util.Set;

/**
 * @description:
 * @author: huangjiarong
 * @create: 2021/9/30
 * @version: v1.0
 */
public interface IResponse {

    /**
     * 指标获取是否为空
     *
     * @return
     */
    boolean isEmpty();

    /**
     * 是否成功
     *
     * @return
     */
    boolean isSuccess();

    /**
     * 获取错误信息
     *
     * @return
     */
    String getError();

    /**
     * 获取服务器实例列表
     *
     * @return
     */
    Set<String> getInstances();

    /**
     * 获取指标值
     *
     * @return
     */
    Double getMetricValue();

    /**
     * 根据instance分组获取值
     *
     * @return
     */
    Map<String, Double> getMetricValueGroup();

}
