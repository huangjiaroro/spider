package com.hexin.cbas.spider.application.dto;

import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @description:
 * @author: huangjiarong
 * @create: 2021/10/14
 * @version: v1.0
 */
@Data
public class ChannelRealTimeMetricDTO {

    /**
     * 链路是否健康
     */
    private Boolean health;

    /**
     * 链路延迟
     */
    private Double lag;

    /**
     * 链路延迟
     */
    private Double lagTime;

    /**
     * 链路吞吐
     **/
    private Double dealRate;

    /**
     * 节点列表
     */
    private List<ChannelNodeMetricDTO> nodes;

    /**
     * 边集合
     */
    private List<EdgeDTO> edges;

    public Double getDealRate() {
        return dealRate;
    }

    public void setDealRate(Double dealRate) {
        if (Objects.isNull(dealRate)){
            return;
        }
        if (Objects.isNull(this.dealRate)) {
            this.dealRate = dealRate;
        } else {
            this.dealRate = Math.max(this.dealRate, dealRate);
        }

    }

    public void addNode(ChannelNodeMetricDTO node) {
        if (CollectionUtils.isEmpty(nodes)) {
            nodes = new ArrayList<>();
        }
        nodes.add(node);
    }


    public void addLag(Double lag) {
        if (lag == null) {
            return;
        }
        if (this.lag == null) {
            this.lag = lag;
        } else {
            this.lag += lag;
        }
    }

    public void addLagTime(Double lagTime) {
        if (lagTime == null) {
            return;
        }
        if (this.lagTime == null) {
            this.lagTime = lagTime;
        } else {
            this.lagTime += lagTime;
        }
    }
}
