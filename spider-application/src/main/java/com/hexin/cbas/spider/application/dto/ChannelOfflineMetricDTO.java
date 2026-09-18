package com.hexin.cbas.spider.application.dto;

import com.hexin.cbas.spider.domain.amount.entity.DataAmountSummary;
import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: huangjiarong
 * @create: 2021/10/14
 * @version: v1.0
 */
@Data
public class ChannelOfflineMetricDTO {
    List<DataAmountSummary> summaries;

    List<String> xLine;
}
