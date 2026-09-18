package com.hexin.cbas.spider.application.schedule;

import com.hexin.cbas.spider.application.dto.ChannelNodeMetricDTO;
import com.hexin.cbas.spider.application.dto.ChannelRealTimeMetricDTO;
import com.hexin.cbas.spider.application.dto.EdgeDTO;
import com.hexin.cbas.spider.application.manager.MetricManager;
import com.hexin.cbas.spider.application.schedule.pojo.ChannelExecuteSchedule;
import com.hexin.cbas.spider.application.service.*;
import com.hexin.cbas.spider.config.HostKey;
import com.hexin.cbas.spider.config.SpiderConfig;
import com.hexin.cbas.spider.constants.DateFormatConstant;
import com.hexin.cbas.spider.core.pojo.AlertMessage;
import com.hexin.cbas.spider.core.pojo.MetricData;
import com.hexin.cbas.spider.core.pojo.MetricResult;
import com.hexin.cbas.spider.core.pojo.NamedThreadFactory;
import com.hexin.cbas.spider.dal.mapper.spider.DataChannelMapper;
import com.hexin.cbas.spider.dal.po.spider.DataChannelPO;
import com.hexin.cbas.spider.dal.po.spider.MetricRecordPO;
import com.hexin.cbas.spider.domain.channel.service.DataChannelDomainService;
import com.hexin.cbas.spider.utils.LogUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: huangjiarong
 * @create: 2021/10/13
 * @version: v1.0
 */
@Service
@DependsOn("spiderConfig")
@SuppressWarnings("all")
public class GraphMetricService implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(GraphMetricService.class);

    private static final ScheduledExecutorService SCHEDULED_METRIC_SERVICE =
            Executors.newSingleThreadScheduledExecutor(new NamedThreadFactory("scheduledMetricService"));
    private Map<Integer, ChannelRealTimeMetricDTO> metricCaches = new ConcurrentHashMap<>();

    private List<ChannelExecuteSchedule> channelExecuteSchedules = new ArrayList<>();

    @Autowired
    SpiderMetricService service;

    @Autowired
    DataGraphRepresentationService graphService;

    @Autowired
    AlertApplicationService alertService;

    @Autowired
    BusinessDayService businessDayService;

    @Autowired
    MetricRecordApplicationService metricRecordApplicationService;

    @Autowired
    DataChannelDomainService dataChannelDomainService;

    @Autowired
    private DataChannelMapper dataChannelMapper;

    @Autowired
    MetricManager metricManager;

    @Autowired
    SpiderConfig spiderConfig;

    /**
     * 重新加载通道执行缓存
     */
    public void loadChannelMetricCache() {
        LogUtil.info("Start To Reload Channel Cache");
        channelExecuteSchedules = graphService.metricChannelSchedule();
        LogUtil.info("Channel Cache Reloaded :" + channelExecuteSchedules);
        metricCaches.clear();
    }

    /**
     * 获取通道监控结果
     *
     * @param channelId
     * @return
     */
    public ChannelRealTimeMetricDTO channelMetric(Integer channelId) {
        //先从缓存获取数据
        ChannelRealTimeMetricDTO result = metricCaches.get(channelId);
        if (result == null) {
            result = doMetricAndAlert(graphService.findChannelSchedule(channelId), false);
        }
        //组装图
        if (!CollectionUtils.isEmpty(result.getNodes())) {
            List<EdgeDTO> edges = new ArrayList<>();
            result.setEdges(edges);
            List<ChannelNodeMetricDTO> roots = result.getNodes().stream().filter(
                    p -> StringUtils.isEmpty(p.getParentId())).collect(Collectors.toList());
            Queue<ChannelNodeMetricDTO> queue = new LinkedList<>();
            queue.addAll(roots);
            while (!queue.isEmpty()) {
                ChannelNodeMetricDTO node = queue.poll();
                List<ChannelNodeMetricDTO> targets = result.getNodes().stream().filter(
                        p -> node.getId().equals(p.getParentId())).collect(Collectors.toList());
                for (ChannelNodeMetricDTO target : targets) {
                    edges.add(new EdgeDTO(node.getId(), target.getId()));
                    queue.add(target);
                }
            }
        }
        return result;
    }

    /**
     * 开始实时监控
     */
    private void startMetric() {
        SCHEDULED_METRIC_SERVICE.scheduleWithFixedDelay(() -> {
            if (LocalTime.now().isBefore(spiderConfig.getStartTime()) || LocalTime.now().isAfter(spiderConfig.getEndTime())) {
                return;
            }
            long start = System.currentTimeMillis();
            LOGGER.info("start to metirc");
            List<ChannelExecuteSchedule> finalChannels = channelExecuteSchedules;
            for (ChannelExecuteSchedule channelExecuteSchedule : finalChannels) {
                try {
                    LocalDateTime now = LocalDateTime.now();
                    //先判断下次执行时间是否为空，为空则先计算下次执行时间
                    if (channelExecuteSchedule.getNextExecuteTime() == null) {
                        channelExecuteSchedule.setNextExecuteTime(computeNextExecuteTime(now, channelExecuteSchedule.getStartTime()
                                , channelExecuteSchedule.getEndTime(), channelExecuteSchedule.isBusiness()
                                , channelExecuteSchedule.getMetricInterval()
                                , channelExecuteSchedule.getUnit()));
                    }
                    //如果当前时间还未到达下次执行时间则不做检测
                    if (channelExecuteSchedule.getNextExecuteTime().isAfter(now)) {
                        continue;
                    }

                    //执行监控告警
                    ChannelRealTimeMetricDTO realTimeMetric = doMetricAndAlert(channelExecuteSchedule, true);

                    if (!Objects.isNull(realTimeMetric.getLagTime())) {
                        metricManager.gauge("channel_lag_guage", "channel_name"
                                , channelExecuteSchedule.getChannel().getName(), realTimeMetric.getLagTime());
                    }

                    //结果放入缓存
                    metricCaches.put(channelExecuteSchedule.getChannel().getId().intValue(), realTimeMetric);

                    //更新下次执行时间
                    channelExecuteSchedule.setNextExecuteTime(computeNextExecuteTime(channelExecuteSchedule.getNextExecuteTime()
                            , channelExecuteSchedule.getStartTime()
                            , channelExecuteSchedule.getEndTime(), channelExecuteSchedule.isBusiness()
                            , channelExecuteSchedule.getMetricInterval()
                            , channelExecuteSchedule.getUnit()));

                    //更新数据库监控状态
                    DataChannelPO dataChannelPO = new DataChannelPO();
                    dataChannelPO.setId(channelExecuteSchedule.getChannel().getId());
                    dataChannelPO.setHealth(realTimeMetric.getHealth());
                    dataChannelMapper.updateByPrimaryKeySelective(dataChannelPO);
                } catch (Exception e) {
                    LOGGER.error("metric error:", e);
                }
            }

            LOGGER.info("end to metirc use time:{} ms", System.currentTimeMillis() - start);
        }, 0, 1, TimeUnit.MINUTES);

    }

    /**
     * 获取监控数据 根据告警配置告警数据是否满足告警要求进行告警
     *
     * @param channelExecuteSchedule
     */
    private ChannelRealTimeMetricDTO doMetricAndAlert(ChannelExecuteSchedule channelExecuteSchedule, boolean alert) {
        ChannelRealTimeMetricDTO realTimeMetric = new ChannelRealTimeMetricDTO();
        realTimeMetric.setHealth(true);
        Set<AlertMessage> alertMessages = new HashSet<>();
        channelExecuteSchedule.getNodes().forEach(p -> {
            ChannelNodeMetricDTO node = new ChannelNodeMetricDTO();
            realTimeMetric.addNode(node);
            node.setChannelId(channelExecuteSchedule.getChannel().getId().intValue());
            node.setId(p.getId());
            node.setNodeName(p.getNodeName());
            node.setNodeType(p.getNodeType());
            node.setParentId(p.getParentId());
            node.setNodeId(p.getNodeId());
            node.setClassify(p.getClassify());
            node.setHealth(true);
            try {
                if (p.executeNode()) {
                    MetricResult metricResult = service.spiderMetric(p, p.getNodeType());
                    node.setHeaders(metricResult.getMetricData());
                    //如果整个节点没有数据,则需要对该节点告警
                    if (CollectionUtils.isEmpty(metricResult.getMetricData())) {
                        node.setHealth(false);
                        realTimeMetric.setHealth(false);
                        alertService.save(new AlertMessage(String.format("通道[%s]的节点[%s]没有监控数据！"
                                , p.getChannelName(), p.getNodeName()), 1, "huangjiarong@myhexin.com", "none"
                                , null, null));
                        return;
                    }

                    List<HostKey> keyList = metricResult.getHostKeySet().stream().sorted().collect(Collectors.toList());
                    List<List<Object>> rows = new ArrayList<>();
                    for (HostKey hostKey : keyList) {
                        List<Object> row = new ArrayList<>();
                        row.add(hostKey);
                        for (MetricData metricDatum : metricResult.getMetricData()) {
                            if (metricDatum.getDetail().containsKey(hostKey)) {
                                row.add(metricDatum.getDetail().get(hostKey));
                            } else {
                                row.add("none");
                            }
                        }
                        rows.add(row);
                    }

                    node.setRows(rows);
                    realTimeMetric.addLag(metricResult.getLag());
                    realTimeMetric.addLagTime(metricResult.getLagTime());
                    realTimeMetric.setDealRate(metricResult.getDeal());
                    alertMessages.addAll(metricResult.getAlertMessages());
                    //是否有告警信息
                    if (!CollectionUtils.isEmpty(metricResult.getAlertMessages())) {
                        realTimeMetric.setHealth(false);
                        node.setHealth(false);
                    }
                }
            } catch (Exception e) {
                realTimeMetric.setHealth(false);
                node.setHealth(false);
                LOGGER.error("real time metric exception", e);
            }
        });

        //是否有告警信息
        if (!CollectionUtils.isEmpty(alertMessages)) {
            //存在则当前告警次数加1
            channelExecuteSchedule.increaseAlert();
        } else {
            //否则清空当前告警次数
            channelExecuteSchedule.resizeAlert();
        }

        if (alert) {
            // 判断告警次数是否到达阈值，如果到达告警阈值则进行告警
            if (channelExecuteSchedule.getSingleAlertMatchTimes() >= channelExecuteSchedule.getChannel().getAlertThreshold()) {
                alertService.save(alertMessages);
                channelExecuteSchedule.resizeAlert();
            }
        }

        //如果开启了监控检查结果入库
        if (DataChannelPO.METRIC_START.equals(channelExecuteSchedule.getChannel().getMetric())) {
            saveMetricRecord(realTimeMetric, channelExecuteSchedule.getChannel().getId());
        }
        return realTimeMetric;
    }

    /**
     * 保存检测结果
     *
     * @param realTimeMetric
     * @param id
     */
    private void saveMetricRecord(ChannelRealTimeMetricDTO realTimeMetric, Long id) {
        try {
            MetricRecordPO recordPO = new MetricRecordPO();
            recordPO.setChannelId(id.intValue());
            recordPO.setHealth(realTimeMetric.getHealth());
            recordPO.setLagTime(realTimeMetric.getLagTime());
            recordPO.setMetricTime(new Date());
            recordPO.setDealRate(realTimeMetric.getDealRate());
            recordPO.setMetricDate(DateFormatUtils.format(recordPO.getMetricTime(), DateFormatConstant.YEAR_MONTH_DAY));
            metricRecordApplicationService.save(recordPO);

            //更新channel的延迟时间和健康状态
            DataChannelPO channel = new DataChannelPO();
            channel.setId(id);
            channel.setLagTime(realTimeMetric.getLagTime());
            channel.setHealth(realTimeMetric.getHealth());
            dataChannelMapper.updateByPrimaryKeySelective(channel);
        } catch (Exception e) {
            LOGGER.error("save metric record exception", e);
        }

    }


    /**
     * 计算下次执行时间
     *
     * @param executeTime
     * @param startTime
     * @param endTime
     * @param isBusiness
     * @param metricInterval
     * @param unit
     * @return
     */
    private LocalDateTime computeNextExecuteTime(LocalDateTime executeTime, LocalTime startTime, LocalTime endTime, boolean isBusiness
            , Integer metricInterval, ChronoUnit unit) {
        LocalDateTime nextExecuteTime = executeTime.plus(metricInterval.longValue(), unit);

        //如果大于结束时间则直接置为第二天的开始时间
        if (nextExecuteTime.toLocalTime().isAfter(endTime)) {
            nextExecuteTime = LocalDateTime.of(nextExecuteTime.toLocalDate(), startTime);
            nextExecuteTime = nextExecuteTime.plus(1, ChronoUnit.DAYS);
            return nextExecuteTime;
        }
        //如果执行时间小于开始时间则置为开始时间
        if (nextExecuteTime.toLocalTime().isBefore(startTime)) {
            nextExecuteTime = LocalDateTime.of(nextExecuteTime.toLocalDate(), startTime);
        }
        //如果是交易日才有数据 且执行当天为非交易日则执行时间置为下一天开始时间
        if (isBusiness && !businessDayService.checkBusinessDay(executeTime.toLocalDate())) {
            nextExecuteTime = LocalDateTime.of(nextExecuteTime.toLocalDate(), startTime);
            nextExecuteTime = nextExecuteTime.plus(1, ChronoUnit.DAYS);
        }
        return nextExecuteTime;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        loadChannelMetricCache();
        startMetric();
    }

}
