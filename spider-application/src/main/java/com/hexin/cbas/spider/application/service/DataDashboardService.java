package com.hexin.cbas.spider.application.service;

import com.hexin.cbas.spider.application.dto.*;
import com.hexin.cbas.spider.constants.DateFormatConstant;
import com.hexin.cbas.spider.constants.MarkConstant;
import com.hexin.cbas.spider.dal.po.spider.DataChannelPO;
import com.hexin.cbas.spider.dal.po.spider.MetricRecordPO;
import com.hexin.cbas.spider.domain.amount.entity.DataAmountSummary;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: huangjiarong
 * @create: 2021/12/18
 * @version: v1.0
 */
@Service
public class DataDashboardService {
    private static final String HEALTH = "健康";
    private static final String UN_HEALTH = "不健康";
    private static final String FIVE_2_TEN = "5-10分钟";
    private static final String TEN = "10分钟以上";
    private static final String NORMAL = "5分钟以下";
    private static final String METRIC = "开启监控";
    private static final String UN_METRIC = "未开启监控";

    @Autowired
    AlertRepresentationService alertRepresentationService;

    @Autowired
    DataAmountRepresentationService dataAmountRepresentationService;

    @Autowired
    MetricRecordRepresentationService metricRecordRepresentationService;

    @Autowired
    DataChannelRepresentationService dataChannelRepresentationService;

    public DashboardViewDTO dashboardView() {
        DashboardViewDTO result = new DashboardViewDTO();
        List<DataChannelPO> runningChannel = dataChannelRepresentationService.runningChannel();
        List<DataChannelPO> metricChannel = runningChannel.stream().filter(p -> DataChannelPO.METRIC_START.equals(p.getMetric())).collect(Collectors.toList());

        //计算每个业务的个数
        result.setChannelView(channelView(runningChannel));
        //计算健康度
        result.setHealthView(healthView(metricChannel, metricChannel.size()));
        //计算延迟度
        result.setLagView(lagView(metricChannel.stream().filter(p -> p.getLagTime() != null).collect(Collectors.toList()), metricChannel.size()));
        //监控率
        result.setMetricView(metricView());
        return result;
    }

    /**
     * 获取各业务线链路数
     *
     * @param channels
     * @return
     */
    private Map<String, Long> channelView(List<DataChannelPO> channels) {
        Map<String, Long> result = new HashMap<>();
        Set<String> types = channels.stream().map(p -> p.getBizLine()).collect(Collectors.toSet());
        types.forEach(t -> result.put(t, channels.stream().filter(c -> t.equals(c.getBizLine())).count()));
        return result;
    }

    /**
     * 获取健康度情况
     *
     * @param channels
     * @param total
     * @return
     */
    private Map<String, Long> healthView(List<DataChannelPO> channels, Integer total) {
        Map<String, Long> result = new HashMap<>();
        result.put(UN_HEALTH, channels.stream().filter(p -> !p.getHealth()).count());
        result.put(HEALTH, total - result.get(UN_HEALTH));
        return result;
    }

    /**
     * 获取延迟度情况:5分钟以下 5-10分钟 10分钟以上
     *
     * @param channels
     * @param total
     * @return
     */
    private Map<String, Long> lagView(List<DataChannelPO> channels, Integer total) {
        Map<String, Long> result = new HashMap<>();
        result.put(FIVE_2_TEN, channels.stream().filter(p -> p.getLagTime() >= 5 && p.getLagTime() < 10).count());
        result.put(TEN, channels.stream().filter(p -> p.getLagTime() >= 10).count());
        result.put(NORMAL, total - result.get(FIVE_2_TEN) - result.get(TEN));
        return result;
    }

    /**
     * 监控率
     *
     * @return
     */
    private Map<String, Long> metricView() {
        Map<String, Long> result = new HashMap<>();
        List<DataChannelPO> runningChannel = dataChannelRepresentationService.runningChannel();
        Long metricSize = runningChannel.stream().filter(p -> DataChannelPO.METRIC_START.equals(p.getMetric())).count();
        result.put(METRIC, metricSize);
        result.put(UN_METRIC, runningChannel.size() - metricSize);
        return result;
    }


    /**
     * 获取采集量图表数据
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public DashboardChartDTO dashboardDataChart(String startDate, String endDate) {
        return dataAmountRepresentationService.dashboardDataChart(startDate, endDate);
    }


    /**
     * 获取dashboard上 tpc的2个指标展示 监控率以图表方式展示
     *
     * @return
     */
    public DashboardTpcDTO dashboardTpcData() {
        return dashboardTpcData(DateFormatUtils.format(DateUtils.addDays(new Date(), -1), DateFormatConstant.YMD));
    }

    /**
     * 获取dashboard上 tpc的2个指标展示 监控率以图表方式展示
     *
     * @return
     */
    public DashboardTpcDTO dashboardTpcData(String date) {
        DashboardTpcDTO result = new DashboardTpcDTO();
        //到达率
        result.setArrivalRate(dataAmountRepresentationService.dashboardAccuracyChart(date, date).getYLine().get(0));
        //延迟时间
        result.setLagTime(metricRecordRepresentationService.lagTime95(date));
        Map<String, Long> metricView = metricView();
        //监控率
        result.setMetricRate(String.format(MarkConstant.DOUBLE_FORMAT, (double) metricView.get(METRIC) / (metricView.get(METRIC) + metricView.get(UN_METRIC)) * 100));
        return result;
    }


    /**
     * 获取到达率图表数据
     *
     * @param startDate
     * @param endDate
     * @return
     */
    @Deprecated
    public DashboardChartDTO dashboardAccuracyChart(String startDate, String endDate) {
        return dataAmountRepresentationService.dashboardAccuracyChart(startDate, endDate);

    }

    /**
     * 获取到达率详情
     *
     * @param date
     * @return
     */
    public List<DataAmountSummary> dashboardAccuracyChartDetail(String date) {
        try {
            return dataAmountRepresentationService.dashboardAccuracyChartDetail(DateFormatUtils.format(DateUtils.parseDate(date, DateFormatConstant.YMD), DateFormatConstant.YEAR_MONTH_DAY));
        } catch (ParseException e) {
            throw new RuntimeException("date parse exception", e);
        }
    }

    /**
     * 获取告警top
     *
     * @return
     */
    public DashboardAlertResultDTO dashboardAlert() {
        return alertRepresentationService.dashboardAlertTop(LocalDate.now().plusDays(-1).format(DateTimeFormatter.ISO_DATE));
    }

    /**
     * 获取健康度图表 健康度计算: 全天健康数(没出现过不健康的) /总量
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public DashboardChartDTO dashboardHealthChart(String startDate, String endDate) {
        DashboardChartDTO result = new DashboardChartDTO();
        List<MetricRecordPO> groups = metricRecordRepresentationService.healthGroup(startDate, endDate);
        result.setXLine(groups
                .stream()
                .map(MetricRecordPO::getMetricDate)
                .distinct()
                .sorted()
                .collect(Collectors.toList()));
        List<String> yLines = new ArrayList<>();
        result.setYLine(yLines);
        result.setData(new ArrayList<>());
        result.getXLine().forEach(date -> {
            long health = groups.stream().filter(p -> date.equals(p.getMetricDate()) && p.getHealth() == null).count();
            long total = groups.stream().filter(p -> date.equals(p.getMetricDate())).count();
            Map<String, Long> detail = new HashMap<>();
            detail.put(UN_HEALTH, total-health);
            detail.put(HEALTH, health);
            result.getData().add(detail);
            yLines.add(String.format(MarkConstant.DOUBLE_FORMAT
                    , (double) health
                            / total * 100));
        });
        return result;
    }

    /**
     * 延迟度计算: 每条链路的最高延迟 小于5分钟 /通道总数
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public DashboardChartDTO dashboardLagTimeChart(String startDate, String endDate) {
        DashboardChartDTO result = new DashboardChartDTO();
        List<MetricRecordPO> groups = metricRecordRepresentationService.lagTimeGroup(startDate, endDate);
        result.setXLine(groups
                .stream()
                .map(MetricRecordPO::getMetricDate)
                .distinct()
                .sorted()
                .collect(Collectors.toList()));

        result.setYLine(new ArrayList<>());
        result.setData(new ArrayList<>());
        result.getXLine().forEach(date -> {
            List<MetricRecordPO> group = groups.stream().filter(p -> date.equals(p.getMetricDate())).collect(Collectors.toList());
            DashboardLagTimeDetailDTO detail = buildDetail(group);
            result.getYLine().add(String.format(MarkConstant.DOUBLE_FORMAT
                    , (double) Integer.valueOf(detail.getNormal())
                            / (Integer.valueOf(detail.getNormal()) + Integer.valueOf(detail.getFive2ten()) + Integer.valueOf(detail.getTen())) * 100));
            result.getData().add(detail);
        });
        return result;
    }


    public DashboardLagTimeDetailDTO dashboardLagTimeChartDetail(String date) {
        List<MetricRecordPO> groups = metricRecordRepresentationService.lagTimeGroup(date, date);
        return buildDetail(groups);
    }

    private DashboardLagTimeDetailDTO buildDetail(List<MetricRecordPO> groups) {
        DashboardLagTimeDetailDTO result = new DashboardLagTimeDetailDTO();
        result.setDetail(groups);
        result.setNormal(String.valueOf(groups.stream().filter(p -> p.getLagTime() < 5).count()));
        result.setFive2ten(String.valueOf(groups.stream().filter(p -> p.getLagTime() >= 5 && p.getLagTime() < 10).count()));
        result.setTen(String.valueOf(groups.stream().filter(p -> p.getLagTime() >= 10).count()));
        return result;
    }
}
