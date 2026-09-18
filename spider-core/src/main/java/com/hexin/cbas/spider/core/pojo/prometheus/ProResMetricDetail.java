package com.hexin.cbas.spider.core.pojo.prometheus;

import lombok.Data;
import lombok.ToString;
import org.springframework.util.StringUtils;

import java.util.Objects;

/**
 * @description:
 * @author: huangjiarong
 * @create: 2021/9/29
 * @version: v1.0
 */
@ToString
@Data
public class ProResMetricDetail {
    private String event;
    private String instance;
    private String job;
    private String name;
    private String host;
    private String tree;
    private Integer partition;

    public String getInstance() {
        if (!Objects.isNull(partition)) {
            return String.valueOf(partition);
        }

        if (StringUtils.isEmpty(host)) {
            return instance;
        }
        return host;

    }
}
