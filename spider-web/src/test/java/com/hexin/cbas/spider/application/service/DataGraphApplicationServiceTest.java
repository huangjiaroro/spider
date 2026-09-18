package com.hexin.cbas.spider.application.service;

import com.hexin.cbas.spider.application.dto.ChannelGraphDTO;
import com.hexin.cbas.spider.application.schedule.GraphMetricService;
import com.hexin.cbas.spider.utils.JsonUtil;
import com.hexin.cbas.spider.web.access.Application;
import com.hexin.cbas.spider.web.dto.ResponseDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @description:
 * @author: huangjiarong
 * @create: 2021/10/13
 * @version: v1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class DataGraphApplicationServiceTest {

    @Autowired
    DataGraphApplicationService service;

    @Autowired
    GraphMetricService metricService;

    @Test
    public void getNodeInfo() {
        System.out.println(JsonUtil.parseString(ResponseDTO.success(service.getNodeInfo("flume", 2))));
    }

    @Test
    public void getNodeMetricInfo() {
        System.out.println(JsonUtil.parseString(ResponseDTO.success(service.getNodeMetricInfo("flume"))));
    }

    @Test
    public void save() {
//        System.out.println(JsonUtil.parseString(ResponseDTO.success(metricService.channelMetric(5))));
//        ChannelGraphDTO channelGraphDTO = service.get(5);
//        System.out.println(JsonUtil.parseString(channelGraphDTO));
//        service.save(channelGraphDTO);
    }

    @Test
    public void get() {
        ChannelGraphDTO channelGraphDTO = service.get(5);
        System.out.println(JsonUtil.parseString(ResponseDTO.success(channelGraphDTO)));
    }
}