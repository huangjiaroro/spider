package com.hexin.cbas.spider.application.schedule.pojo;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * @description:
 * @author: huangjiarong
 * @create: 2021/12/16
 * @version: v1.0
 */
@Data
public class UploadResponse {
    private static final int SUCCESS = 0;

    @JsonProperty("status_code")
    private Integer statusCode;

    @JsonProperty("status_msg")
    private String statusMsg;

    @JsonProperty("msg")
    private String msg;

    public boolean success() {
        return SUCCESS == statusCode;
    }
}
