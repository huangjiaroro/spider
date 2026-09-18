package com.hexin.cbas.spider.core.alert;


import com.hexin.cbas.spider.constants.MarkConstant;
import com.hexin.cbas.spider.core.pojo.MetricMeta;
import com.hexin.cbas.spider.enums.OperatorEnum;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @description:
 * @author: huangjiarong
 * @create: 2021/9/16
 * @version: v1.0
 */
public class AlertParser {

    private static final String REPLACE = "\\$\\{%s\\}";
    private static final String NONE_REPLACE = ",%s=\"\\$\\{%s\\}\"";
    private static final String ALERT_MESSAGE = "您负责的{%s}链路->{%s}节点:{%s}指标异常  metricKey{%s} 当前值:{%s} 告警阈值:{%s} 请关注链路情况！";
    private static final String ALERT_MESSAGE_WITH_INSTANCE = "您负责的{%s}链路->{%s}节点{%s}实例:{%s}指标异常  metricKey{%s} 当前值:{%s} 告警阈值:{%s} 请关注链路情况！";
    private static final String ALERT_EXCEPTION_MESSAGE = "您配置的{%s}通道{%s}节点的指标{%s}获取指标数据异常 metricKey{%s} reason:{%s}";
    private static final String ALERT_NON_DATA_MESSAGE = "您配置的{%s}通道{%s}节点的指标{%s}未获取到指标结果 metricKey:{%s}";
    private static final String ALERT_OFFLINE_ARRIVAL_MESSAGE = "{%s},{%s}通道的完整性为{%s},请及时处理";
    private static final String ALERT_OFFLINE_EARLIER_MESSAGE = "{%s},{%s}通道的日环比为{%s},请及时处理";

    private static final String ALERT_MESSAGE_HOST_NOT_USE = "您负责的{%s}链路->{%s}节点: 服务器{%s} 已经下线，请上平台确认！";

    private static final String ALERT_MESSAGE_HOST_NEW_LINE = "您负责的{%s}链路->{%s}节点: 服务器{%s} 新上线，请上平台确认！";
    private static final String NONE_VALUE = "none";

    /**
     * 组装监控的key
     * metricInfo 中如flume 的source 和sink 可能有多个，所以需要对其进行拆分生成多个指标key
     *
     * @param metricMeta
     * @param metricInfo
     * @return
     */
    public static List<String> parseMetricKey(MetricMeta metricMeta, Map<String, Object> metricInfo) {

        String metricKey = metricMeta.getMetricKey();
        String[] linkColumns = metricMeta.getLinkColumn().split(MarkConstant.SPLIT);
        Queue<String> queue = new LinkedList<>();
        queue.add(metricKey);
        for (String linkColumn : linkColumns) {
            String[] values = StringUtils.isEmpty(metricInfo.get(linkColumn)) ? new String[]{NONE_VALUE} : metricInfo.get(linkColumn).toString().split(MarkConstant.SPLIT);
            Queue<String> middleQueue = new LinkedList<>();
            while (!queue.isEmpty()) {
                String key = queue.poll();
                for (String value : values) {
                    if (StringUtils.isEmpty(value)) {
                        continue;
                    }
                    if (NONE_VALUE.equals(value)) {
                        middleQueue.add(key.replaceAll(String.format(NONE_REPLACE, linkColumn, linkColumn), ""));
                    } else {
                        middleQueue.add(key.replaceAll(String.format(REPLACE, linkColumn), value));
                    }
                }
            }
            queue = middleQueue;
        }
        return new ArrayList<>(queue);
    }

    /**
     * 判断是否需要告警
     *
     * @param value      指标值
     * @param operator   操作符
     * @param alertValue 告警阈值
     * @return
     */
    public static boolean parseAlert(Double value, OperatorEnum operator, int alertValue) {
        switch (operator) {
            case EQUAL:
                return value == (double) alertValue;
            case GRATER:
                return value > alertValue;
            case LESS:
                return value < alertValue;
            default:
                return false;
        }
    }


    /**
     * 构建告警內容
     *
     * @param channelName
     * @param nodeName
     * @param metricKeyName
     * @param nodeValue
     * @param alterValue
     * @param metricKey
     * @return
     */
    public static String buildAlertMessage(String channelName, String nodeName, String metricKeyName, double nodeValue, int alterValue, String metricKey) {
        return String.format(ALERT_MESSAGE, channelName, nodeName, metricKeyName, metricKey, String.format(MarkConstant.DOUBLE_FORMAT, nodeValue), alterValue);
    }

    /**
     * 构建告警內容
     *
     * @param channelName
     * @param nodeName
     * @param metricKeyName
     * @param nodeValue
     * @param alterValue
     * @param metricKey
     * @return
     */
    public static String buildAlertMessage(String instance, String channelName, String nodeName, String metricKeyName, double nodeValue, int alterValue, String metricKey) {
        return String.format(ALERT_MESSAGE_WITH_INSTANCE, channelName, nodeName, instance, metricKeyName, metricKey, String.format(MarkConstant.DOUBLE_FORMAT, nodeValue), alterValue);
    }


    public static String buildHostNotUseAlertMessage(String hostname, String channelName, String nodeName) {
        return String.format(ALERT_MESSAGE_HOST_NOT_USE, channelName, nodeName, hostname);
    }

    public static String buildHostNewUseAlertMessage(String hostname, String channelName, String nodeName) {
        return String.format(ALERT_MESSAGE_HOST_NEW_LINE, channelName, nodeName, hostname);
    }

    /**
     * 构建告警內容
     *
     * @param channelName
     * @param nodeName
     * @param metricValue
     * @param metricKey
     * @param error
     * @return
     */
    public static String buildChannelExceptionMessage(String channelName, String nodeName, String metricValue, String metricKey, String error) {
        return String.format(ALERT_EXCEPTION_MESSAGE, channelName, nodeName, metricValue, metricKey, error);
    }


    /**
     * 构建告警內容
     *
     * @param channelName
     * @param nodeName
     * @param metricValue
     * @param metricKey
     * @return
     */
    public static String buildChannelNonDataMessage(String channelName, String nodeName, String metricValue, String metricKey) {
        return String.format(ALERT_NON_DATA_MESSAGE, channelName, nodeName, metricValue, metricKey);
    }


    public static String buildArrivalMessage(String date, String channelName, String arrival) {
        return String.format(ALERT_OFFLINE_ARRIVAL_MESSAGE, date, channelName, arrival);
    }

    public static String buildEarlierMessage(String date, String channelName, String earlier) {
        return String.format(ALERT_OFFLINE_EARLIER_MESSAGE, date, channelName, earlier);
    }
}

