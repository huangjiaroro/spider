package com.hexin.cbas.spider.application.schedule;

import com.hexin.cbas.spider.application.dto.ChannelGraphDTO;
import com.hexin.cbas.spider.application.dto.ChannelNodeDTO;
import com.hexin.cbas.spider.application.service.AlertApplicationService;
import com.hexin.cbas.spider.application.service.BusinessDayService;
import com.hexin.cbas.spider.application.service.DataGraphApplicationService;
import com.hexin.cbas.spider.application.service.SpiderMetricService;
import com.hexin.cbas.spider.constants.DateFormatConstant;
import com.hexin.cbas.spider.constants.MarkConstant;
import com.hexin.cbas.spider.core.pojo.AlertMessage;
import com.hexin.cbas.spider.core.pojo.MetricElementNode;
import com.hexin.cbas.spider.dal.mapper.spider.MetricMetaMapper;
import com.hexin.cbas.spider.dal.po.spider.MetricMetaPO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @description: info1 小时同比率告警
 * @author: huangjiarong
 * @create: 2022/2/15
 * @version: v1.0
 */
@Deprecated
@SuppressWarnings("all")
public class HourCounterCheckerService {

    private static final String ALERT_PERSON = "huangjiarong@myhexin.com,wanghujia@myhexin.com,zhengkeda@myhexin.com,zhongxiaoqing@myhexin.com";
    private static final String ALERT_HOUR = "%s链路%s点同比率为%s,阈值%s,今日小时采集量%s,同比采集量%s";

    private static final Logger LOGGER = LoggerFactory.getLogger(HourCounterCheckerService.class);

    private Integer metricMetaHourId = 14;
    private Integer info1Id = 46;


    @Autowired
    BusinessDayService businessDayService;
    @Autowired
    DataGraphApplicationService graphApplicationService;
    @Autowired
    MetricMetaMapper metricMetaMapper;
    @Autowired
    SpiderMetricService metricService;
    @Autowired
    AlertApplicationService alertApplicationService;

    /**
     * 上一次同一时间的总量缓存
     */
    private Map<Integer, Long> cache = new HashMap<>();


    public void hourCheck() {
        LocalDateTime dateTime = LocalDateTime.now();
        //非交易日不做每小时同比校验
        if (businessDayService.checkBusinessDay(dateTime.toLocalDate())) {
            return;
        }
        LOGGER.info("start to Hour check .....................");
        ChannelGraphDTO graph = graphApplicationService.get(info1Id);
        LocalTime start = LocalTime.parse(graph.getMetricStart(), DateTimeFormatter.ofPattern(DateFormatConstant.HOUR_MINUTE));
        LocalTime end = LocalTime.parse(graph.getMetricEnd(), DateTimeFormatter.ofPattern(DateFormatConstant.HOUR_MINUTE));
        if (dateTime.toLocalTime().isBefore(start)) {
            return;
        }
        if (dateTime.toLocalTime().isAfter(end)) {
            return;
        }
        graphApplicationService.buildExecuteNodeGraph(graph);
        List<ChannelNodeDTO> flumeSinks = graph.getSinks().stream().filter(n -> "flume".equals(n.getNodeType())).collect(Collectors.toList());
        long todayHourOut = channelHourOut(flumeSinks, dateTime);
        Integer key = dateTime.getHour();
        long beforeDayHourOut = cache.containsKey(key) ? cache.get(key) : channelHourOut(flumeSinks, dateTime.plusDays(-1));
        cache.put(key, todayHourOut);
        if (beforeDayHourOut <= 0) {
            return;
        }
        Double rate = (double) todayHourOut / (double) beforeDayHourOut * 100;
        if (Double.valueOf(graph.getEarlierRate().replaceAll(MarkConstant.PERCENT, "")) > rate) {
            AlertMessage message = new AlertMessage();
            message.setChannelName(graph.getName());
            message.setAlertContent(String.format(ALERT_HOUR, graph.getName(), key, String.format(MarkConstant.DOUBLE_FORMAT, rate) + MarkConstant.PERCENT, graph.getEarlierRate(), todayHourOut, beforeDayHourOut));
            message.setAlertLevel(1);
            message.setAlertTime(new Date());
            for (String alertPerson : ALERT_PERSON.split(MarkConstant.SPLIT)) {
                //同比比较告警
                message.setAlertPerson(alertPerson);
                alertApplicationService.save(message);
            }
        }


    }


    private long channelHourOut(List<ChannelNodeDTO> flumeSinks, LocalDateTime dateTime) {
        long offlineValue = 0;
        for (ChannelNodeDTO sink : flumeSinks) {
            offlineValue += hourOut(sink, dateTime).longValue();
        }
        return offlineValue;
    }


    private Double hourOut(ChannelNodeDTO node, LocalDateTime dateTime) {
        List<MetricElementNode> metricNodes = new ArrayList<>();
        node.setElementNodes(metricNodes);
        MetricMetaPO metricMeta = metricMetaMapper.selectByPrimaryKey(metricMetaHourId);
        MetricElementNode elementNode = new MetricElementNode();
        elementNode.setMetricMeta(metricMeta);
        metricNodes.add(elementNode);
        return metricService.spiderMetric(node, node.getNodeType(),
                Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant())).getOfflineOut();
    }


}
