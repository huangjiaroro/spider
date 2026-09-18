package com.hexin.cbas.spider.dal.po.spider;

import com.hexin.cbas.spider.core.pojo.AlertMessage;
import lombok.Data;

import java.util.Date;

/**
 * @author huangjiarong
 */
@Data
public class AlertRecordPO extends AlertMessage {

    public static final Integer ALERTED = 1;

    private Integer id;

    private Integer alertState;

    private Integer alertRetry;

    private Date createTime;

    private String errorReason;
}