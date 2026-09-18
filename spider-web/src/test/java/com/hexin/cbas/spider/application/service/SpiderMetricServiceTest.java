package com.hexin.cbas.spider.application.service;

import com.hexin.cbas.spider.application.schedule.OfflineMetricService;
import com.hexin.cbas.spider.dal.mapper.spider.AlertRecordMapper;
import com.hexin.cbas.spider.utils.JsonUtil;
import com.hexin.cbas.spider.web.access.Application;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: huangjiarong
 * @create: 2021/10/11
 * @version: v1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class SpiderMetricServiceTest {

    @Autowired
    SpiderMetricService service;


    @Autowired
    OfflineMetricService offlineMetricService;

    @Autowired
    AlertRecordMapper alertRecordMapper;

    @Autowired
    AlertRepresentationService alertRepresentationService;

    @Test
    public void spiderMetric() throws Exception {
//        LocalDate date = LocalDate.now();
//        for (int i = 0; i < 2; i++) {
//            offlineMetricService.offlineMetric(date.plusDays(-1 * i));
//        }
//        Thread.currentThread().join();
    }
}