package com.hexin.cbas.spider.application.dto;

import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: huangjiarong
 * @create: 2021/12/18
 * @version: v1.0
 */
@Data
public class DashboardChartDTO {

    List<String> xLine;
    List<String> yLine;
    List<Object> data;
}
