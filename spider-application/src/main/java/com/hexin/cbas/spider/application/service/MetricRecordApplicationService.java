package com.hexin.cbas.spider.application.service;

import com.hexin.cbas.spider.dal.mapper.spider.MetricRecordMapper;
import com.hexin.cbas.spider.dal.po.spider.MetricRecordPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

/**
 * @description:
 * @author: huangjiarong
 * @create: 2021/10/14
 * @version: v1.0
 */
@Service
public class MetricRecordApplicationService {

    @Autowired
    MetricRecordMapper mapper;

    @Transactional(rollbackFor = Exception.class)
    public void save(MetricRecordPO record) {
        if (record.getId() == null) {
            mapper.insertSelective(record);
        } else {
            mapper.updateByPrimaryKeySelective(record);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void clearMetricRecord(Integer saveDay) {
        LocalDate metricDate = LocalDate.now().plusDays(-1L * saveDay);
        mapper.clearMetricRecord(metricDate);
    }
}
