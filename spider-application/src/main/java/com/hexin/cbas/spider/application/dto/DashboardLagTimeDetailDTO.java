package com.hexin.cbas.spider.application.dto;

import com.hexin.cbas.spider.dal.po.spider.MetricRecordPO;
import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: huangjiarong
 * @create: 2021/12/18
 * @version: v1.0
 */
@Data
public class DashboardLagTimeDetailDTO {
    private String normal;
    private String five2ten;
    private String ten;
    List<MetricRecordPO> detail;
}
