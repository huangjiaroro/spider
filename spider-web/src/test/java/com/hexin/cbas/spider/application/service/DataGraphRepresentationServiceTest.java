package com.hexin.cbas.spider.application.service;

import com.hexin.cbas.spider.application.dto.ChannelOfflineMetricDTO;
import com.hexin.cbas.spider.application.schedule.pojo.ChannelExecuteSchedule;
import com.hexin.cbas.spider.utils.JsonUtil;
import com.hexin.cbas.spider.web.access.Application;
import com.hexin.cbas.spider.web.dto.ResponseDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @description:
 * @author: huangjiarong
 * @create: 2021/10/14
 * @version: v1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class DataGraphRepresentationServiceTest {

    @Autowired
    DataGraphRepresentationService service;

    @Test
    public void metricChannel() {
        List<ChannelExecuteSchedule> channelExecuteSchedules = service.metricChannelSchedule();
        System.out.println(channelExecuteSchedules);
    }

    @Test
    public void getOfflineMetric() {
//        ChannelOfflineMetricDTO offlineMetric = service.getOfflineMetric(5, startDate, endDate);
//        System.out.println(JsonUtil.parseString(ResponseDTO.success(offlineMetric)));
    }
}