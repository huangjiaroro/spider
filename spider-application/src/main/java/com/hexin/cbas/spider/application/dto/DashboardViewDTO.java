package com.hexin.cbas.spider.application.dto;

import lombok.Data;

import java.util.Map;

/**
 * @description:
 * @author: huangjiarong
 * @create: 2021/12/18
 * @version: v1.0
 */
@Data
public class DashboardViewDTO {

    Map<String,Long> channelView;

    Map<String,Long> healthView;

    Map<String,Long> lagView;

    Map<String,Long> metricView;

}
