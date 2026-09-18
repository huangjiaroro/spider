package com.hexin.cbas.spider.application.service;

import com.hexin.cbas.spider.constants.DateFormatConstant;
import com.hexin.cbas.spider.dal.mapper.spider.MetricRecordMapper;
import com.hexin.cbas.spider.dal.po.spider.MetricRecordPO;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;

/**
 * @description:
 * @author: huangjiarong
 * @create: 2021/10/14
 * @version: v1.0
 */
@Service
public class MetricRecordRepresentationService {

    @Autowired
    MetricRecordMapper mapper;

    /**
     * 获取95%的延迟时间
     *
     * @param date
     * @return
     */
    public String lagTime95(String date) {
        try {
            date = DateFormatUtils.format(DateUtils.parseDate(date, DateFormatConstant.YMD), DateFormatConstant.YEAR_MONTH_DAY);
            List<MetricRecordPO> group = mapper.lagTimeGroup(date, date);
            if (group.size() <= 0) {
                return "0.00";
            }
            group.sort((group1, group2) -> (int) (group1.getLagTime() - group2.getLagTime()));
            int index = (int) (group.size() * 0.05 + 1);
            return group.get(index).getLagTime().toString();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

    }

    public List<MetricRecordPO> healthGroup(String startDate, String endDate) {
        try {
            startDate = DateFormatUtils.format(DateUtils.parseDate(startDate, DateFormatConstant.YMD), DateFormatConstant.YEAR_MONTH_DAY);
            endDate = DateFormatUtils.format(DateUtils.parseDate(endDate, DateFormatConstant.YMD), DateFormatConstant.YEAR_MONTH_DAY);
            return mapper.healthGroup(startDate, endDate);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<MetricRecordPO> lagTimeGroup(String startDate, String endDate) {
        try {
            startDate = DateFormatUtils.format(DateUtils.parseDate(startDate, DateFormatConstant.YMD), DateFormatConstant.YEAR_MONTH_DAY);
            endDate = DateFormatUtils.format(DateUtils.parseDate(endDate, DateFormatConstant.YMD), DateFormatConstant.YEAR_MONTH_DAY);
            return mapper.lagTimeGroup(startDate, endDate);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
