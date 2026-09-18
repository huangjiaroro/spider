package com.hexin.cbas.spider.dal.po.spider;

import lombok.Data;

@Data
public class DataAmountSummaryPO {
    private Long id;

    private Long channelId;

    private String channelName;

    private String pDate;

    private Long sourceAmountReport;

    private Long sourceAmount;

    private Long collectAmount;

    private String arrivalRate;

    private String earlierRate;
}