package com.hexin.cbas.spider.dal.po.spider;

import com.hexin.cbas.spider.core.pojo.MetricMeta;
import lombok.Data;

/**
 * @author huangjiarong
 */
@Data
public class MetricMetaPO extends MetricMeta {
    private Integer id;
    private String metricType;
}