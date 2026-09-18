package com.hexin.cbas.spider.application.dto;

import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: huangjiarong
 * @create: 2021/12/20
 * @version: v1.0
 */
@Data
public class DashboardAlertResultDTO {
    List<String> top;
    List<DashboardAlertDTO> alerts;
}
