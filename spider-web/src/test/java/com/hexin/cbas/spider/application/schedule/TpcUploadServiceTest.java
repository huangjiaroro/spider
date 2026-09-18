package com.hexin.cbas.spider.application.schedule;

import com.hexin.cbas.spider.web.access.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

/**
 * @description:
 * @author: huangjiarong
 * @create: 2022/1/6
 * @version: v1.0
 */
@SuppressWarnings("all")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class TpcUploadServiceTest {

    @Autowired
    TpcUploadService service;
    @Autowired
    OfflineMetricService offlineMetricService;

    @Test
    public void uploadTpcTarget() {
        offlineMetricService.report(LocalDate.of(2022,9,21));
//        service.uploadTpcTarget(LocalDate.now().plusDays(-1));
    }
}