package com.hexin.cbas.spider.application.schedule;

import com.hexin.cbas.spider.application.dto.ChannelGraphDTO;
import com.hexin.cbas.spider.application.dto.ChannelNodeDTO;
import com.hexin.cbas.spider.application.service.*;
import com.hexin.cbas.spider.constants.DateFormatConstant;
import com.hexin.cbas.spider.constants.MarkConstant;
import com.hexin.cbas.spider.core.alert.AlertParser;
import com.hexin.cbas.spider.core.pojo.AlertMessage;
import com.hexin.cbas.spider.core.pojo.MetricElementNode;
import com.hexin.cbas.spider.core.pojo.MetricResult;
import com.hexin.cbas.spider.core.pojo.NamedThreadFactory;
import com.hexin.cbas.spider.dal.mapper.spider.ReportMapper;
import com.hexin.cbas.spider.dal.po.spider.DataChannelPO;
import com.hexin.cbas.spider.dal.po.spider.MetricMetaPO;
import com.hexin.cbas.spider.dal.po.spider.ReportPO;
import com.hexin.cbas.spider.domain.amount.entity.DataAmountSummary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: huangjiarong
 * @create: 2021/10/15
 * @version: v1.0
 */
@Service
@SuppressWarnings("all")
public class OfflineMetricService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OfflineMetricService.class);
    private static final int TIMEOUT = 5;

    @Autowired
    DataGraphRepresentationService graphService;

    @Autowired
    DataGraphApplicationService graphApplicationService;

    @Autowired
    AlertApplicationService alertService;

    @Autowired
    SpiderMetricService metricService;

    @Autowired
    DataAmountApplicationService dataAmountApplicationService;

    @Autowired
    DataAmountRepresentationService dataAmountRepresentationService;

    @Autowired
    BusinessDayService businessDayService;

    @Autowired
    DataUploadService dataUploadService;

    private ReportMapper reportMapper;

    public OfflineMetricService(ReportMapper reportMapper) {
        this.reportMapper = reportMapper;
    }

    private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(10, new NamedThreadFactory("offlineExecutorService"));

    /**
     * 离线指标计算每天凌晨1点计算昨天的所有监控通道
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void offlineMetric() {
        LocalDate now = LocalDate.now();
        offlineMetric(now);
        report(now);
    }


    /**
     * 生成报表
     *
     * @param date
     * @return void
     * @author huangjiarong@myhexin.com
     * @date 2023/9/22 14:26
     **/
    @Transactional(rollbackFor = Exception.class)
    public void report(LocalDate date) {
        String reportDay = date.plusDays(-1).format(DateTimeFormatter.ofPattern(DateFormatConstant.YEAR_MONTH_DAY));
        ReportPO report = reportMapper.report(reportDay);
        report.checkArrivalRate();
        reportMapper.deleteByPrimaryDate(reportDay);
        report.setPDate(reportDay);
        reportMapper.insert(report);

    }

    /**
     * 执行离线指标对比监控
     *
     * @param date
     */
    public void offlineMetric(LocalDate date) {
        LocalDate metricDay = date.plusDays(-1);
        //获取所有需要监控的节点
        List<DataChannelPO> channels = graphService.metricChannel();
        CountDownLatch latch = new CountDownLatch(channels.size());
        channels.forEach(p -> EXECUTOR_SERVICE.execute(() -> {

            try {
                //判断前一天是否在监控日期范围内,如果不在则不需要进行监控
                if (p.businessMetric() && !businessDayService.checkBusinessDay(metricDay)) {
                    return;
                }
                //获取通道拓扑
                ChannelGraphDTO graph = graphApplicationService.get(p.getId().intValue());

                DataAmountSummary dataAmountSummary = new DataAmountSummary();
                dataAmountSummary.setChannelId(p.getId());
                dataAmountSummary.setPDate(metricDay.format(DateTimeFormatter.ofPattern(DateFormatConstant.YEAR_MONTH_DAY)));
                dataAmountSummary.setSourceAmount(0L);

                //构建只有执行节点的新图
                graphApplicationService.buildExecuteNodeGraph(graph);

                //当只有一个节点时特殊处理:出入都按照该节点的处理数计算，即到达率默认都为100%，主要是为了计算离线数据量
                offlineOutCompute(graph, dataAmountSummary, date);
                if (graph.getNodes().size() <= 1) {
                    dataAmountSummary.setSourceAmount(dataAmountSummary.getCollectAmount());
                } else {
                    offlineInCompute(graph, dataAmountSummary, date);
                }

                //如果没有开启完整性校验则不需要进行到达率告警
                if (p.integrityCheck()) {
                    //CBAS-5828 feat 当源端数据为0时默认到达率改为100% 有些链路业务方确实没有数据，会在实时告警时体现出来
                    Double arrivalRate = 100.00;
                    //计算到达率
                    if (dataAmountSummary.getSourceAmount() > 0) {
                        arrivalRate = (double) dataAmountSummary.getCollectAmount() / dataAmountSummary.getSourceAmount() * 100;
                    }
                    dataAmountSummary.setArrivalRate(String.format(MarkConstant.DOUBLE_FORMAT, arrivalRate));
                    alert(p, dataAmountSummary, arrivalRate, metricDay);
                }
                //需要进行同比率计算和告警
                earlierRateAlert(dataAmountSummary, p, metricDay);
                //保存结果
                dataAmountApplicationService.save(dataAmountSummary);
            } catch (Exception e) {
                LOGGER.error(p.getName() + " offline metric exception", e);
            } finally {
                latch.countDown();
            }
        }));
        try {
            if (latch.await(TIMEOUT, TimeUnit.MINUTES)) {
                LOGGER.info("too long time to execute ");
            }
        } catch (Exception e) {
            LOGGER.error("offline metric exception", e);
        }
        //计算完成后进行上报
//        dataUploadService.dataUpload(metricDay);
    }


    private void offlineInCompute(ChannelGraphDTO graph, DataAmountSummary dataAmountSummary, LocalDate date) {
        //获取首节点
        List<ChannelNodeDTO> roots = graph.getRoots();
        for (ChannelNodeDTO root : roots) {
            //获取首节点的离线输入指标
            Double offlineIn = offlineIn(root, date);
            //如果离线输入指标不存在也可以取离线输出指标作为替代
            if (offlineIn <= 0) {
                offlineIn = offlineOut(root, date);
            }
            dataAmountSummary.setSourceAmount(dataAmountSummary.getSourceAmount() + offlineIn.longValue());
        }
    }

    private void offlineOutCompute(ChannelGraphDTO graph, DataAmountSummary dataAmountSummary, LocalDate date) {

        //获取sink节点
        List<ChannelNodeDTO> flumeSinks = graph.getSinks().stream().filter(n -> "flume".equals(n.getNodeType())).collect(Collectors.toList());

        Double offlineOutValue = 0.00;
        for (ChannelNodeDTO sink : flumeSinks) {
            long offlineValue = offlineOut(sink, date).longValue();
            offlineOutValue += offlineValue;
        }
        dataAmountSummary.setCollectAmount(offlineOutValue.longValue());
    }


    /**
     * 到达率,同比率告警
     *
     * @param channel
     * @param dataAmountSummary
     * @param arrivalRate
     * @param metricDay
     */
    private void alert(DataChannelPO channel, DataAmountSummary dataAmountSummary, Double arrivalRate, LocalDate metricDay) {
        AlertMessage message = new AlertMessage();
        message.setAlertPerson(channel.getCreator());
        message.setAlertLevel(1);
        message.setAlertTime(new Date());

        //到达率告警
        if (Double.valueOf(channel.getArrivalRate().replaceAll(MarkConstant.PERCENT, "")) > arrivalRate) {
            message.setAlertContent(AlertParser.buildArrivalMessage(dataAmountSummary.getPDate(), channel.getName(), dataAmountSummary.getArrivalRate()));
            alertService.save(message);
        }

        earlierRateAlert(dataAmountSummary, channel, metricDay);
    }

    private void earlierRateAlert(DataAmountSummary dataAmountSummary, DataChannelPO channel, LocalDate metricDay) {
        AlertMessage message = new AlertMessage();
        message.setAlertPerson(channel.getCreator());
        message.setAlertLevel(1);
        message.setAlertTime(new Date());

        LocalDate compareDay = selectCompareDay(metricDay, businessDayService.checkBusinessDay(metricDay), true);
        if (Objects.isNull(compareDay)) {
            return;
        }
        /**
         * 计算同比率 先获取前一个小于当前日期的最新日期 对于交易日通道 非交易日没有数据
         * DataAmountSummary nearestAmount =  dataAmountRepresentationService.selectAmountByChannelIdAndDate()
         * CBAS-7693 feat 同比告警改为环比告警  计算环比率
         **/
        DataAmountSummary nearestAmount = dataAmountRepresentationService.selectAmountByChannelIdAndDate(dataAmountSummary.getChannelId()
                , compareDay.format(DateTimeFormatter.ofPattern(DateFormatConstant.YEAR_MONTH_DAY)));

        Double earlierRate = 100.00;
        if (nearestAmount != null) {
            if (nearestAmount.getCollectAmount() > 0) {
                earlierRate = (double) dataAmountSummary.getCollectAmount() / nearestAmount.getCollectAmount() * 100;
            }
            dataAmountSummary.setEarlierRate(String.format(MarkConstant.DOUBLE_FORMAT, earlierRate));
            //同比率告警
            if (Double.valueOf(channel.getEarlierRate().replaceAll(MarkConstant.PERCENT, "")) > earlierRate) {
                message.setAlertContent(AlertParser.buildEarlierMessage(dataAmountSummary.getPDate(), channel.getName(), dataAmountSummary.getEarlierRate()));
                alertService.save(message);
            }
        }
    }


    /**
     * 获取节点离线输入指标值
     *
     * @param node
     * @param date
     * @return
     */
    private Double offlineIn(ChannelNodeDTO node, LocalDate date) {
        Double offline = 0.00;
        for (MetricMetaPO n : graphApplicationService.nodeOfflineInTarget(node.getNodeType())) {
            offline = Math.max(offlineResult(n, node, date).getOfflineIn(), offline);
        }
        return offline;
    }


    /**
     * 获取节点离线指标输出值
     *
     * @param node
     * @param date
     * @return
     */
    private Double offlineOut(ChannelNodeDTO node, LocalDate date) {
        Double offline = 0.00;
        for (MetricMetaPO n : graphApplicationService.nodeOfflineOutTarget(node.getNodeType())) {
            offline = Math.max(offlineResult(n, node, date).getOfflineOut(), offline);
        }
        return offline;
    }

    private MetricResult offlineResult(MetricMetaPO n, ChannelNodeDTO node, LocalDate date) {
        List<MetricElementNode> metricNodes = new ArrayList<>();
        node.setElementNodes(metricNodes);
        MetricElementNode elementNode = new MetricElementNode();
        elementNode.setMetricMeta(n);
        metricNodes.add(elementNode);
        return metricService.spiderMetric(node, node.getNodeType(), Date.from(date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
    }

    /**
     * 计算需要和那一天做比较
     * 逻辑：先获取上周同一天 如果同时交易日或者同是非交易日，则返回该日
     * 否则时间往后取一天再做比较
     * 如果一周都匹配不到则取上上周相同逻辑匹配出一天
     * 还是找不到则默认当天为正常
     */
    private LocalDate selectCompareDay(LocalDate date, boolean isBusinessDay, boolean continueTry) {
        LocalDate compareDay = date.plusWeeks(-1);
        while (compareDay.isBefore(date)) {
            if (businessDayService.checkBusinessDay(compareDay) == isBusinessDay) {
                return compareDay;
            }
            compareDay = compareDay.plusDays(1);
        }
        if (continueTry) {
            return selectCompareDay(date.plusWeeks(-1), isBusinessDay, false);
        }
        return null;
    }

}

