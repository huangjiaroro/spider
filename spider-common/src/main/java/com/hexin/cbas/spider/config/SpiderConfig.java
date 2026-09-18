package com.hexin.cbas.spider.config;

import com.hexin.cbas.spider.constants.DateFormatConstant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description:
 * @author: huangjiarong
 * @create: 2021/10/19
 * @version: v1.0
 */
@Component
@SuppressWarnings("all")
public class SpiderConfig {

    public static String PROMETHEUS_ADDRESS;
    public static Set<String> IP_WHITE_LIST;
    public static volatile Map<String, Set<HostKey>> ARRIVAL_HOST=new ConcurrentHashMap<>();

    public static final String HOST_NAME;
    private static final String HOST_IP;
    private static final String MONITOR_AGENT_PORT;
    private static final String MONITOR_AGENT_PATH;

    public static final String MONITOR_API;


    @Value("${spider.vanish-address}")
    private String vanishAddress;

    @Value("${spider.alert.start-time}")
    private String alertStartTime;

    @Value("${spider.alert.end-time}")
    private String alertEndTime;

    @Value("${spider.alert.ip-white-list:}")
    private Set<String> ipWhiteList;

    @Value("${spider.prometheus-address}")
    private String prometheusAddress;

    private LocalTime startTime;
    private LocalTime endTime;

    static {
        HOST_NAME = System.getenv("SERVICE_NAME") + "@k8s-" + System.getenv("THS_CLUSTER");
        HOST_IP = System.getenv("HOST_IP");
        MONITOR_AGENT_PORT = System.getenv("MONITOR_AGENT_PORT");
        MONITOR_AGENT_PATH = System.getenv("MONITOR_AGENT_PATH");
        MONITOR_API = "http://" + HOST_IP + ":" + MONITOR_AGENT_PORT + "/" + MONITOR_AGENT_PATH;
    }

    @PostConstruct
    public void init() {
        if (ipWhiteList == null) {
            ipWhiteList = new HashSet<>();
        }
        IP_WHITE_LIST = ipWhiteList;
        PROMETHEUS_ADDRESS = prometheusAddress;
    }

    public String getVanishAddress() {
        return vanishAddress;
    }

    public LocalTime getStartTime() {
        if (startTime == null) {
            startTime = LocalTime.parse(alertStartTime, DateTimeFormatter.ofPattern(DateFormatConstant.HOUR_MINUTE));
        }
        return startTime;

    }

    public LocalTime getEndTime() {
        if (endTime == null) {
            endTime = LocalTime.parse(alertEndTime, DateTimeFormatter.ofPattern(DateFormatConstant.HOUR_MINUTE));
        }
        return endTime;
    }

    public static void setArrivalHostCache(Map<String, Set<HostKey>> hostCache) {
        ARRIVAL_HOST.clear();
        ARRIVAL_HOST.putAll(hostCache);
    }
}
