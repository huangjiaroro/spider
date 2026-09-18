package com.hexin.cbas.spider.dal.po.spider;

import lombok.Data;

/**
 * 指标po
 * @author huangjiarong@myhexin.com
 * @date 2023/9/21 20:01
 **/
@Data
public class ReportPO {
    public static final double MAX_RATE = 100.00;
    private Integer id;

    private Integer channelNum;

    private Long channelDataTotal;

    private Double channelThroughput;

    private Double channelLag;

    private Double channelArrivalRate;
    /**
     * 链路接入时长
     **/
    private int channelAccessTime;

    private String pDate;

    public void checkArrivalRate() {
        this.channelArrivalRate=Math.min(MAX_RATE,this.channelArrivalRate);
    }
}