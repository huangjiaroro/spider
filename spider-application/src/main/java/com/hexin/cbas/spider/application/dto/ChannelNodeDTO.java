package com.hexin.cbas.spider.application.dto;

import com.hexin.cbas.spider.core.pojo.MetricElement;
import lombok.Data;

import java.util.Map;

/**
 * @description:
 * @author: huangjiarong
 * @create: 2021/10/13
 * @version: v1.0
 */
@Data
public class ChannelNodeDTO extends MetricElement {

    private String id;

    private Integer nodeId;

    private String parentId;

    private String nodeType;

    private String name;

    private Boolean sourceNode;

    private Map<String, Object> drawJson;


}
