package com.hexin.cbas.spider.application.service;

import com.hexin.cbas.spider.application.assembler.DataChannelGraphAssembler;
import com.hexin.cbas.spider.application.dto.ChannelOfflineMetricDTO;
import com.hexin.cbas.spider.application.schedule.pojo.ChannelExecuteSchedule;
import com.hexin.cbas.spider.constants.DateFormatConstant;
import com.hexin.cbas.spider.core.pojo.MetricElementNode;
import com.hexin.cbas.spider.dal.mapper.spider.ChannelNodeMapper;
import com.hexin.cbas.spider.dal.mapper.spider.DataAmountSummaryMapper;
import com.hexin.cbas.spider.dal.mapper.spider.DataChannelMapper;
import com.hexin.cbas.spider.dal.mapper.spider.MetricMetaMapper;
import com.hexin.cbas.spider.dal.po.spider.DataChannelPO;
import com.hexin.cbas.spider.domain.amount.convert.DataAmountSummaryConvert;
import com.hexin.cbas.spider.domain.amount.entity.DataAmountSummary;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: huangjiarong
 * @create: 2021/10/14
 * @version: v1.0
 */
@Service
public class DataGraphRepresentationService {

    @Autowired
    DataChannelMapper channelMapper;

    @Autowired
    ChannelNodeMapper channelNodeMapper;

    @Autowired
    DataChannelGraphAssembler channelGraphAssembler;

    @Autowired
    MetricMetaMapper metricMetaMapper;

    @Autowired
    DataAmountSummaryMapper dataAmountSummaryMapper;

    @Autowired
    DataAmountSummaryConvert convert;

    /**
     * 获取监控通道执行计划
     *
     * @return
     */
    public List<ChannelExecuteSchedule> metricChannelSchedule() {
        return metricChannel().stream().map(this::toChannelExecuteSchedule).collect(Collectors.toList());
    }

    /**
     * 获取某一个通道执行计划
     *
     * @param channelId
     * @return
     */
    public ChannelExecuteSchedule findChannelSchedule(Integer channelId) {
        return toChannelExecuteSchedule(channelMapper.selectByPrimaryKey(channelId.longValue()));
    }

    /**
     * 获取开启监控的通道列表
     *
     * @return
     */
    public List<DataChannelPO> metricChannel() {
        return channelMapper.selectRunningChannel().stream().filter(p -> DataChannelPO.METRIC_START.equals(p.getMetric())).collect(Collectors.toList());
    }

    private ChannelExecuteSchedule toChannelExecuteSchedule(DataChannelPO channelPO) {
        ChannelExecuteSchedule schedule = new ChannelExecuteSchedule();
        schedule.setChannel(channelPO);
        schedule.setMetricInterval(channelPO.getMetricInterval());
        schedule.setUnit(channelPO.toChronoUnit());
        schedule.setNodes(channelNodeMapper.selectByChannelId(channelPO.getId().intValue()).stream().map(node -> channelGraphAssembler.toEntity(node))
                .collect(Collectors.toList()));
        schedule.getNodes().forEach(node -> {
            if (!CollectionUtils.isEmpty(node.getElementNodes())) {
                for (MetricElementNode metricElementNode : node.getElementNodes()) {
                    metricElementNode.setMetricMeta(metricMetaMapper.selectByPrimaryKey(metricElementNode.getMetricId()));
                }
            }
        });
        schedule.setBusiness(channelPO.businessMetric());
        //设置开始时间
        if (StringUtils.isEmpty(channelPO.getMetricStart())) {
            schedule.setStartTime(LocalTime.MIN);
        } else {
            schedule.setStartTime(LocalTime.parse(channelPO.getMetricStart(), DateTimeFormatter.ofPattern(DateFormatConstant.HOUR_MINUTE)));
        }
        //设置结束时间
        if (StringUtils.isEmpty(channelPO.getMetricEnd())) {
            schedule.setEndTime(LocalTime.MAX);
        } else {
            schedule.setEndTime(LocalTime.parse(channelPO.getMetricEnd(), DateTimeFormatter.ofPattern(DateFormatConstant.HOUR_MINUTE)));
        }
        return schedule;
    }

    /**
     * 获取离线指标数据
     *
     * @param channelId
     * @param startDate
     * @param endDate
     * @return
     */
    public ChannelOfflineMetricDTO getOfflineMetric(Integer channelId, String startDate, String endDate) {
        ChannelOfflineMetricDTO result = new ChannelOfflineMetricDTO();
        try {
            List<DataAmountSummary> summaries = dataAmountSummaryMapper.list(channelId.longValue()
                    , DateFormatUtils.format(DateUtils.parseDate(startDate, DateFormatConstant.YMD), DateFormatConstant.YEAR_MONTH_DAY), DateFormatUtils.format(DateUtils.parseDate(endDate, DateFormatConstant.YMD), DateFormatConstant.YEAR_MONTH_DAY))
                    .stream()
                    .map(p -> convert.toEntiy(p)).collect(Collectors.toList());
            summaries.sort(Comparator.comparingInt(o -> Integer.valueOf(o.getPDate())));
            result.setSummaries(summaries);
            result.setXLine(summaries.stream().map(DataAmountSummary::getPDate).collect(Collectors.toList()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}
