package com.hexin.cbas.spider.application.service;

import com.hexin.cbas.spider.application.dto.*;
import com.hexin.cbas.spider.domain.amount.entity.DataAmountSummary;
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
 * @create: 2021/12/20
 * @version: v1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class DataDashboardServiceTest {

    @Autowired
    DataDashboardService dashboardService;

    @Test
    public void dashboardView() {
        DashboardViewDTO dashboardViewDTO = dashboardService.dashboardView();
        System.out.println(JsonUtil.parseString(ResponseDTO.success(dashboardViewDTO)));
    }

    @Test
    public void dashboardDataChart() {
        DashboardChartDTO dashboardChartDTO = dashboardService.dashboardDataChart("2021-12-06", "2021-12-20");
        System.out.println(JsonUtil.parseString(ResponseDTO.success(dashboardChartDTO)));
    }

    @Test
    public void dashboardAccuracyChart() {
//        DashboardChartDTO dashboardChartDTO = dashboardService.dashboardAccuracyChart("2021-12-06", "2021-12-20");
//        System.out.println(JsonUtil.parseString(ResponseDTO.success(dashboardChartDTO)));

        List<DataAmountSummary> summaries = dashboardService.dashboardAccuracyChartDetail("2022-01-12");
    }


    @Test
    public void dashboardAlertTop() {
        DashboardAlertResultDTO resultDTO = dashboardService.dashboardAlert();
        System.out.println(JsonUtil.parseString(ResponseDTO.success(resultDTO)));
    }


    @Test
    public void dashboardHealthChart() {
        DashboardChartDTO dashboardChartDTO = dashboardService.dashboardHealthChart("2022-01-04", "2022-01-05");
        System.out.println(JsonUtil.parseString(ResponseDTO.success(dashboardChartDTO)));
    }


    @Test
    public void dashboardLagTimeChart() {
        DashboardChartDTO dashboardChartDTO = dashboardService.dashboardLagTimeChart("2022-01-04", "2022-01-05");
        System.out.println(JsonUtil.parseString(ResponseDTO.success(dashboardChartDTO)));
    }

    @Test
    public void dashboardLagTimeChartDetail() {
        DashboardLagTimeDetailDTO dashboardLagTimeDetailDTO = dashboardService.dashboardLagTimeChartDetail("2022-01-05");
        System.out.println(JsonUtil.parseString(ResponseDTO.success(dashboardLagTimeDetailDTO)));
    }

    @Test
    public void dashboardTpcData() {
        DashboardTpcDTO dashboardTpcDTO = dashboardService.dashboardTpcData();
        System.out.println(JsonUtil.parseString(ResponseDTO.success(dashboardTpcDTO)));
    }
}