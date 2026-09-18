package com.hexin.cbas.spider.core.node;


import com.alibaba.fastjson.JSON;
import com.hexin.cbas.spider.config.HostKey;
import com.hexin.cbas.spider.config.SpiderConfig;
import com.hexin.cbas.spider.constants.MarkConstant;
import com.hexin.cbas.spider.core.alert.AlertParser;
import com.hexin.cbas.spider.core.client.IClient;
import com.hexin.cbas.spider.core.pojo.*;
import com.hexin.cbas.spider.enums.OperatorEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @description:
 * @author: huangjiarong
 * @create: 2021/9/15
 * @version: v1.0
 */
@SuppressWarnings("all")
public abstract class AbstractNode implements Node {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractNode.class);

    private IClient client;
    private MetricElement element;

    public AbstractNode(IClient client, MetricElement element) {
        this.client = client;
        this.element = element;
    }

    @Override
    public MetricResult metric(Date metricDate) {
        MetricResult result = new MetricResult();
        List<MetricElementNode> elementNodes = element.getElementNodes();
        if (CollectionUtils.isEmpty(elementNodes)) {
            return result;
        }

        for (MetricElementNode elementNode : elementNodes) {
            List<String> metricKeys = AlertParser.parseMetricKey(elementNode.getMetricMeta(), element.getMetricInfos());
            for (String metricKey : metricKeys) {
                try {
                    //获取指标结果
                    IResponse response = doMetric(metricKey, metricDate);
                    if (!response.isSuccess()) {
                        throw new RuntimeException("获取指标出错:" + response.getError());
                    }
                    if (response.isEmpty()) {
                        throw new RuntimeException("获取指标为空:" + metricKey + response.getError());
                    }
                    //如果是延迟指标
                    if (elementNode.getMetricMeta().lagTarget()) {
                        result.addLag(response.getMetricValue());
                    }

                    //如果是实时处理指标
                    if (elementNode.getMetricMeta().dealTarget()) {
                        result.addDeal(response.getMetricValue());
                    }

                    //如果是离线输入指标
                    if (elementNode.getMetricMeta().offlineInTarget()) {
                        result.addOfflineIn(response.getMetricValue());
                    }

                    //如果是离线输出指标
                    if (elementNode.getMetricMeta().offlineOutTarget()) {
                        result.addOfflineOut(response.getMetricValue());
                    }

                    this.alert(response, result.getAlertMessages(), elementNode, result.getMetricData(), metricKey);
                } catch (Exception e) {
                    LOGGER.error(element.getChannelName() + " metric exception", e);
                }
            }
        }
        //计算延迟时间
        result.computeLagTime();
        return result;
    }


    @Override
    public MetricResult metric() {
        return metric(null);
    }

    /**
     * 组装告警信息
     *
     * @param response
     * @param alertMessages
     * @param elementNode
     * @param metricData
     * @param metricKey
     */
    private void alert(IResponse response, List<AlertMessage> alertMessages, MetricElementNode elementNode,
                       List<MetricData> metricData, String metricKey) {


        //没有配置告警规则时不需要进行告警
        if (elementNode.getOperator() == null) {
            return;
        }
        Map<String, Double> metricValueGroup = response.getMetricValueGroup();

        Double metricValue = response.getMetricValue();
        //添加信息当前节点
        MetricData data = MetricData.build(metricValue, elementNode.getMetricMeta().getMetricValue()
                , elementNode.getMetricMeta().getTargetUnit(), metricKey);
        metricData.add(data);

        String[] alertPersons = element.getAlertPerson().split(MarkConstant.SPLIT);

        Map<HostKey, Double> group = new HashMap<>(metricValueGroup.size());
        //处理源端详情 如果是源端节点,则重新处理metricValueGroup 服务ip为cmdb上的服务器ip
        if (Boolean.TRUE.equals(element.getSourceNode())) {
            Set<HostKey> ips = SpiderConfig.ARRIVAL_HOST.get(element.getWorkTree());
            LOGGER.info("workTree:{} data:{}", element.getWorkTree(), JSON.toJSONString(ips));
            for (HostKey host : ips) {
                group.put(host, metricValueGroup.get(host.getExportIp()));
            }
        } else {
            metricValueGroup.forEach((key, value) -> group.put(HostKey.builder()
                    .exportIp(key)
                    .inUse(true)
                    .onlineConfirm(true)
                    .build(), value));
        }


        if (elementNode.isDetailAlert()) {
            group.forEach((key, value) -> {
                if (Objects.isNull(value)) {
                    value = Double.valueOf("0.00");
                    group.put(key, value);
                }

                boolean health = false;

                //如果服务器不使用了,但是一直开发还没有确认就直接进行告警
                if (Boolean.FALSE.equals(key.getInUse())) {
                    String alertMessage = AlertParser.buildHostNotUseAlertMessage(key.getHostName(), element.getChannelName()
                            , element.getNodeName());
                    addAlertMessage(alertMessages, alertPersons, alertMessage, element.getAlertLevel(), metricKey);
                } else if (Boolean.FALSE.equals(key.getOnlineConfirm())) {
                    String alertMessage = AlertParser.buildHostNewUseAlertMessage(key.getHostName(), element.getChannelName()
                            , element.getNodeName());
                    addAlertMessage(alertMessages, alertPersons, alertMessage, element.getAlertLevel(), metricKey);
                } else {
                    //开始每个实例对比告警
                    health = doAlert(StringUtils.isEmpty(key.getExportIp()) ? key.getHostName() : key.getExportIp(), value
                            , elementNode.getOperator(), elementNode.getAlertValue(), data.getKey(), alertPersons, alertMessages, metricKey);
                }

                if (data.isHealth()) {
                    data.setHealth(health);
                }
            });

        } else {
            //对指标总量进行告警
            data.setHealth(doAlert(metricValue, elementNode.getOperator(),
                    elementNode.getAlertValue(), data.getKey(), alertPersons, alertMessages, metricKey));
        }
        data.metricDetail(group);
    }

    /**
     * 执行监控
     *
     * @param instance
     * @param metricValue
     * @param operator
     * @param alertValue
     * @param metricName
     * @param alertPersons
     * @param alertMessages
     * @param metricKey
     * @return 返回是否健康 有告警则为不健康 返回false 否则返回true
     */
    private boolean doAlert(String instance, Double metricValue, OperatorEnum operator,
                            int alertValue, String metricName, String[] alertPersons, List<AlertMessage> alertMessages, String metricKey) {
        if (AlertParser.parseAlert(metricValue, operator, alertValue)) {
            String alertMessage = AlertParser.buildAlertMessage(instance, element.getChannelName()
                    , element.getNodeName(), metricName, metricValue, alertValue, metricKey);
            addAlertMessage(alertMessages, alertPersons, alertMessage, element.getAlertLevel(), metricKey);
            return false;
        }
        return true;
    }


    /**
     * 执行监控
     *
     * @param metricValue
     * @param operator
     * @param alertValue
     * @param metricName
     * @param alertPersons
     * @param alertMessages
     * @param metricKey
     * @return 返回是否健康 有告警则为不健康 返回false 否则返回true
     */
    private boolean doAlert(Double metricValue, OperatorEnum operator, int alertValue, String metricName, String[] alertPersons, List<AlertMessage> alertMessages, String metricKey) {
        if (AlertParser.parseAlert(metricValue, operator, alertValue)) {
            String alertMessage = AlertParser.buildAlertMessage(element.getChannelName(), element.getNodeName(), metricName, metricValue, alertValue, metricKey);
            addAlertMessage(alertMessages, alertPersons, alertMessage, element.getAlertLevel(), metricKey);
            return false;
        }
        return true;
    }

    private void addAlertMessage(List<AlertMessage> alertMessages, String[] alertPersons, String alertMessage, Integer alertLevel, String metricKey) {
        //如果是源端节点,alertPerson填写的是paas告警实例
        for (String alertPerson : alertPersons) {
            alertMessages.add(new AlertMessage(alertMessage, alertLevel, alertPerson, metricKey, element.getChannelName()
                    , Boolean.TRUE.equals(element.getSourceNode()) ? alertPerson : null));
        }
    }

    /**
     * 获取监控数据
     *
     * @param metricKey
     * @param metricDate
     * @return
     */
    protected abstract IResponse doMetric(String metricKey, Date metricDate);

    /**
     * 返回Client
     *
     * @return
     */
    @Override
    public IClient getClient() {
        return client;
    }

    /**
     * 返回element
     *
     * @return
     */
    @Override
    public MetricElement getElement() {
        return element;
    }

}
