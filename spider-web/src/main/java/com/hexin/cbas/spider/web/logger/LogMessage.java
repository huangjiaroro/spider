package com.hexin.cbas.spider.web.logger;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 日志实体
 *
 * @author wanghujia
 * @since 2021/9/16
 */
@Data
@Builder
@AllArgsConstructor
class LogMessage implements Serializable {

    private Long time;

    private String logtime;

    private String traceId;

    private Long cost;

    private String uri;

    private String level;

    private String message;

    private String loginUser;

    private static final String FLAG = "@";
}
