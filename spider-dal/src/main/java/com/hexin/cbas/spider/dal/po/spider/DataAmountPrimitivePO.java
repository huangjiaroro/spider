package com.hexin.cbas.spider.dal.po.spider;

import lombok.Data;

import java.util.Date;

@Data
public class DataAmountPrimitivePO {
    private Long id;

    private Long channelId;

    private String instanceId;

    private String pDate;

    private Long logAmount;

    private Date uploadTime;
}