package com.hexin.cbas.spider.enums;

/**
 * @description: 运算符
 * @author: huangjiarong
 * @create: 2021/9/29
 * @version: v1.0
 */
public enum OperatorEnum {

    /**
     * 等于 =
     */
    EQUAL("="),
    /**
     * 大于 >
     */
    GRATER(">"),
    /**
     * 小于 <
     */
    LESS("<");
    private String description;

    OperatorEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
