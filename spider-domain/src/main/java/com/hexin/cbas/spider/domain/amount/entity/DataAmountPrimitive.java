package com.hexin.cbas.spider.domain.amount.entity;

import lombok.Data;

import java.util.Date;

@Data
public class DataAmountPrimitive {
    private Long id;

    private Long channelId;

    private String instanceId;

    private String pDate;

    private Long logAmount;

    private Date uploadTime;
}