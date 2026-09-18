package com.hexin.cbas.spider.core.pojo;

import com.hexin.cbas.spider.config.HostKey;
import com.hexin.cbas.spider.constants.MarkConstant;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @description: 监控结果返回
 * @author: huangjiarong
 * @create: 2021/9/30
 * @version: v1.0
 */
@ToString
public class MetricResult {

    /**
     * 告警信息
     */
    private List<AlertMessage> alertMessages = new ArrayList<>();

    /**
     * 监控结果列表
     */
    private List<MetricData> metricData = new ArrayList<>();

    private Double lag = 0.00;

    private Double lagTime = 0.00;

    private Double offlineIn;

    private Double offlineOut;

    private Double deal = 0.00;

    public Set<HostKey> getHostKeySet() {
        int maxSize = 0;
        Set<HostKey> keySet = new HashSet<>();
        for (MetricData metricDatum : metricData) {
            if (metricDatum.getDetail() != null && metricDatum.getDetail().size() > maxSize) {
                maxSize = metricDatum.getDetail().size();
                keySet = metricDatum.getDetail().keySet();
            }
        }
        return keySet;
    }

    public Double getDeal() {
        return deal;
    }

    public void addDeal(Double deal) {
        this.deal += deal;
    }


    public void addLag(Double lag) {
        this.lag += lag;
    }

    public void computeLagTime() {
        if (this.deal > 0) {
            this.lagTime = this.lag / this.deal;
        }
    }

    public void addOfflineIn(Double offlineIn) {
        if (this.offlineIn == null) {
            this.offlineIn = offlineIn;
        } else {
            this.offlineIn += offlineIn;
        }

    }

    public void addOfflineOut(Double offlineOut) {
        if (this.offlineOut == null) {
            this.offlineOut = offlineOut;
        } else {
            this.offlineOut += offlineOut;
        }

    }

    public Double getOfflineIn() {
        return offlineIn == null ? 0.00 : offlineIn;
    }

    public void setOfflineIn(Double offlineIn) {
        this.offlineIn = offlineIn;
    }

    public Double getOfflineOut() {
        return offlineOut == null ? 0.00 : offlineOut;
    }

    public void setOfflineOut(Double offlineOut) {
        this.offlineOut = offlineOut;
    }

    public Double getLag() {
        return lag;
    }

    public Double getLagTime() {
        if (lagTime != null) {
            return Double.valueOf(String.format(MarkConstant.DOUBLE_FORMAT, lagTime / 60));
        }
        return lagTime;
    }

    public void setLag(Double lag) {
        this.lag = lag;
    }

    public List<AlertMessage> getAlertMessages() {
        return alertMessages;
    }

    public void setAlertMessages(List<AlertMessage> alertMessages) {
        this.alertMessages = alertMessages;
    }

    public List<MetricData> getMetricData() {
        return metricData;
    }

    public void setMetricData(List<MetricData> metricData) {
        this.metricData = metricData;
    }
}
