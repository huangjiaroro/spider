package com.hexin.cbas.spider.core.node;

/**
 * @description:
 * @author: huangjiarong
 * @create: 2022/1/4
 * @version: v1.0
 */
public interface ExecuteNode {
    default String buildSql() {
        return "";
    }
}
