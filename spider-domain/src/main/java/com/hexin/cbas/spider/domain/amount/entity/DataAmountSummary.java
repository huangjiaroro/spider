package com.hexin.cbas.spider.domain.amount.entity;

import lombok.Data;

@Data
public class DataAmountSummary {
    private Long id;

    private Long channelId;

    private String channelName;

    private String pDate;

    private Long sourceAmount;

    private Long collectAmount;

    private String arrivalRate;

    private String earlierRate;
}