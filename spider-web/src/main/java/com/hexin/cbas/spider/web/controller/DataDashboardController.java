package com.hexin.cbas.spider.web.controller;

import com.hexin.cbas.spider.application.service.DataDashboardService;
import com.hexin.cbas.spider.web.dto.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: huangjiarong
 * @create: 2021/12/8
 * @version: v1.0
 */
@RestController
@RequestMapping("${api.prefix}/dashboard")
public class DataDashboardController {
    @Autowired
    DataDashboardService service;


    /**
     * 获取实时链路总览
     *
     * @return
     */
    @GetMapping("/view")
    public ResponseDTO dashboardView() {
        return ResponseDTO.success(service.dashboardView());
    }


    /**
     * 获取告警TOP信息
     *
     * @return
     */
    @GetMapping("/alert")
    public ResponseDTO dashboardAlertTop() {
        return ResponseDTO.success(service.dashboardAlert());
    }

    /**
     * 获取采集量图表数据
     *
     * @return
     */
    @GetMapping("/chart/data")
    public ResponseDTO dashboardDataChart(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) {
        return ResponseDTO.success(service.dashboardDataChart(startDate, endDate));
    }


    /**
     * 获取tpc的三个指标
     *
     * @return
     */
    @GetMapping("/data/tpc")
    @Deprecated
    public ResponseDTO dashboardTpcData() {
        return ResponseDTO.success(service.dashboardTpcData());
    }

    /**
     * 获取准确率图详情
     *
     * @return
     */
    @GetMapping("/chart/accuracy/detail")
    public ResponseDTO dashboardAccuracyChartDetail(@RequestParam("date") String date) {
        return ResponseDTO.success(service.dashboardAccuracyChartDetail(date));
    }

    /**
     * 获取健康度图表
     *
     * @return
     */
    @GetMapping("/chart/health")
    public ResponseDTO dashboardHealthChart(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) {
        return ResponseDTO.success(service.dashboardHealthChart(startDate, endDate));
    }


    /**
     * 获取延迟时间图表
     *
     * @return
     */
    @GetMapping("/chart/lag")
    public ResponseDTO dashboardLagTimeChart(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) {
        return ResponseDTO.success(service.dashboardLagTimeChart(startDate, endDate));
    }

    /**
     * 获取延迟时间详情
     *
     * @return
     */
    @GetMapping("/chart/lag/detail")
    public ResponseDTO dashboardLagTimeChartDetail(@RequestParam("date") String date) {
        return ResponseDTO.success(service.dashboardLagTimeChartDetail(date));
    }

}
