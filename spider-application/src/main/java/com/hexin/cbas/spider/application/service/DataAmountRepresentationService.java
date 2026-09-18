package com.hexin.cbas.spider.application.service;

import com.hexin.cbas.spider.application.dto.DashboardChartDTO;
import com.hexin.cbas.spider.constants.DateFormatConstant;
import com.hexin.cbas.spider.constants.MarkConstant;
import com.hexin.cbas.spider.dal.mapper.spider.DataAmountSummaryMapper;
import com.hexin.cbas.spider.dal.po.spider.DataAmountSummaryPO;
import com.hexin.cbas.spider.domain.amount.convert.DataAmountSummaryConvert;
import com.hexin.cbas.spider.domain.amount.entity.DataAmountSummary;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: huangjiarong
 * @create: 2021/10/18
 * @version: v1.0
 */
@Service
public class DataAmountRepresentationService {

    @Autowired
    DataAmountSummaryMapper summaryMapper;

    @Autowired
    DataAmountSummaryConvert dataAmountSummaryConvert;

    /**
     * 获取最近时间的记录
     *
     * @param channelId
     * @param pDate
     * @return
     */
    public DataAmountSummary nearestAmount(Long channelId, String pDate) {
        return dataAmountSummaryConvert.toEntiy(summaryMapper.selectNearestAmount(channelId, pDate));
    }


    public DataAmountSummary selectAmountByChannelIdAndDate(Long channelId, String pDate) {
        return dataAmountSummaryConvert.toEntiy(summaryMapper.selectAmountByChannelIdAndDate(channelId, pDate));
    }

    public DashboardChartDTO dashboardDataChart(String startDate, String endDate) {
        DashboardChartDTO result = new DashboardChartDTO();
        List<DataAmountSummaryPO> summaryGroup = summaryGroup(startDate, endDate);
        result.setXLine(summaryGroup.stream().map(DataAmountSummaryPO::getPDate).collect(Collectors.toList()));
        result.setYLine(summaryGroup.stream().map(p -> p.getCollectAmount().toString()).collect(Collectors.toList()));
        return result;
    }

    public DashboardChartDTO dashboardAccuracyChart(String startDate, String endDate) {
        DashboardChartDTO result = new DashboardChartDTO();
        List<DataAmountSummaryPO> summaryGroup = summaryGroup(startDate, endDate);
        result.setXLine(summaryGroup.stream().map(DataAmountSummaryPO::getPDate).collect(Collectors.toList()));
        result.setYLine(summaryGroup.stream().map(p -> String.format(MarkConstant.DOUBLE_FORMAT, Double.valueOf(p.getArrivalRate()))).collect(Collectors.toList()));
        return result;
    }

    private List<DataAmountSummaryPO> summaryGroup(String startDate, String endDate) {
        try {
            startDate = DateFormatUtils.format(DateUtils.parseDate(startDate, DateFormatConstant.YMD), DateFormatConstant.YEAR_MONTH_DAY);
            endDate = DateFormatUtils.format(DateUtils.parseDate(endDate, DateFormatConstant.YMD), DateFormatConstant.YEAR_MONTH_DAY);
            List<DataAmountSummaryPO> summaryGroup = summaryMapper.summaryGroup(startDate, endDate);
            summaryGroup.sort(Comparator.comparingInt(o -> Integer.valueOf(o.getPDate())));
            return summaryGroup;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public List<DataAmountSummary> dashboardAccuracyChartDetail(String date) {
        return summaryMapper.list(null, date, date).stream().map(p -> dataAmountSummaryConvert.toEntiy(p)).filter(p-> StringUtils.isNotEmpty(p.getArrivalRate())).collect(Collectors.toList());
    }

}