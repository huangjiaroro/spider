package com.hexin.cbas.spider.core.pojo.prometheus;


import com.hexin.cbas.spider.core.pojo.IResponse;
import lombok.ToString;

import java.util.Map;
import java.util.Set;

/**
 * @description: prometheus 返回结果封装
 * @author: huangjiarong
 * @create: 2021/9/29
 * @version: v1.0
 */
@ToString
public class PrometheusResponse implements IResponse {

    private static final String SUCCESS = "success";

    String status;
    ProResData data;
    String errorType;
    String error;


    @Override
    public boolean isEmpty() {
        return data.getResult().isEmpty();
    }

    @Override
    public boolean isSuccess() {
        return SUCCESS.equals(status);
    }

    @Override
    public String getError() {
        return error;
    }


    @Override
    public Set<String> getInstances() {
        return data.getInstances();
    }

    @Override
    public Double getMetricValue() {
        return data.getMetricValue();
    }

    @Override
    public Map<String, Double> getMetricValueGroup() {
        return data.getMetricValueGroup();
    }


    public String getErrorType() {
        return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ProResData getData() {
        return data;
    }

    public void setData(ProResData data) {
        this.data = data;
    }
}
