package com.hexin.cbas.spider.dal.po.spider;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * @description:
 * @author: huangjiarong
 * @create: 2021/12/16
 * @version: v1.0
 */
@Data
@EqualsAndHashCode(of = {"hostName","exporterIp"})
public class TreeHostInfoPO {
    private static final int HOST_ONLINE_STATE = 0;

    private Integer id;

    /**
     * 机房编号
     */
    @JsonProperty("room_code")
    private String roomCode;

    /**
     * 机房名称
     */
    @JsonProperty("room_name")
    private String roomName;

    /**
     * 设备名
     */
    @JsonProperty("host_name")
    private String hostName;


    /**
     * 主机状态 0-在线状态，1-维护状态，5-停用状态
     */
    @JsonProperty("host_status")
    private Integer hostStatus;

    /**
     * 业务编码
     */
    @JsonProperty("business_code")
    private String businessCode;

    /**
     * 业务名
     */
    @JsonProperty("business_name")
    private String businessName;


    /**
     * 业务大类编码
     */
    @JsonProperty("class_code")
    private String classCode;

    /**
     * 业务大类名
     */
    @JsonProperty("class_name")
    private String className;

    /**
     * 远程登录地址
     */
    @JsonProperty("login_address")
    private String loginAddress;

    /**
     * 负责人
     */
    private String department;

    /**
     * 内网IP，多个IP以","分割
     */
    @JsonProperty("I")
    private String interIp;

    /**
     * 外网IP，多个IP以","分割
     */
    @JsonProperty("P")
    private String outerIp;


    /**
     * 公网IP，多个IP以","分割
     */
    @JsonProperty("G")
    private String publicIp;

    private boolean online;

    private String exporterIp;

    private Boolean inUse;

    private Boolean onlineConfirm;



    public boolean inUse() {
        return HOST_ONLINE_STATE == hostStatus;
    }

}
