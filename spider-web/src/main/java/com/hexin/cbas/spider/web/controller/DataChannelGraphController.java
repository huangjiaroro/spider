package com.hexin.cbas.spider.web.controller;

import com.alibaba.fastjson.JSON;
import com.hexin.cbas.spider.application.dto.ChannelGraphDTO;
import com.hexin.cbas.spider.application.schedule.CacheService;
import com.hexin.cbas.spider.application.schedule.GraphMetricService;
import com.hexin.cbas.spider.application.schedule.OfflineMetricService;
import com.hexin.cbas.spider.application.schedule.TpcUploadService;
import com.hexin.cbas.spider.application.service.BusinessServerInfoApplicationService;
import com.hexin.cbas.spider.application.service.DataGraphApplicationService;
import com.hexin.cbas.spider.application.service.DataGraphRepresentationService;
import com.hexin.cbas.spider.constants.DateFormatConstant;
import com.hexin.cbas.spider.utils.LogUtil;
import com.hexin.cbas.spider.web.dto.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @description:
 * @author: huangjiarong
 * @create: 2021/10/13
 * @version: v1.0
 */
@RestController
@RequestMapping("${api.prefix}/dataChannelGraph")
@SuppressWarnings("all")
public class DataChannelGraphController {

    @Autowired
    private DataGraphApplicationService service;

    @Autowired
    private DataGraphRepresentationService graphRepresentationService;

    @Autowired
    private GraphMetricService metricService;

    @Autowired
    private OfflineMetricService offlineMetricService;

    @Autowired
    private TpcUploadService tpcUploadService;

    @Autowired
    private CacheService cacheService;

    @Autowired
    BusinessServerInfoApplicationService businessServerInfoApplicationService;


    /**
     * 节点属性
     *
     * @return
     */
    @GetMapping("/nodeInfo")
    public ResponseDTO getNodeInfo(@RequestParam String nodeType, @RequestParam Integer classify) {
        return ResponseDTO.success(service.getNodeInfo(nodeType, classify));
    }


    /**
     * 获取监控属性
     *
     * @return
     */
    @GetMapping("/nodeMetricInfo")
    public ResponseDTO getNodeMetricInfo(@RequestParam String nodeType) {
        return ResponseDTO.success(service.getNodeMetricInfo(nodeType));
    }


    /**
     * 保存通道拓扑
     *
     * @return
     */
    @PostMapping("/save")
    public ResponseDTO saveChannelGraph(@RequestBody ChannelGraphDTO channelGraphDTO) {
        LogUtil.info("保存通道拓扑开始，入参：【{}】", JSON.toJSONString(channelGraphDTO));
        service.save(channelGraphDTO);
        //更新缓存
        cacheService.cacheHostInfo();
        metricService.loadChannelMetricCache();
        LogUtil.info("保存通道拓扑结束");
        return ResponseDTO.success();
    }

    /**
     * 获取通道拓扑
     *
     * @return
     */
    @GetMapping("/getByChannelId")
    public ResponseDTO getChannelGraph(@RequestParam Integer channelId) {
        return ResponseDTO.success(service.get(channelId));
    }

    /**
     * 获取通道实时指标
     *
     * @return
     */
    @GetMapping("/metric/realTime")
    public ResponseDTO getRealTimeMetric(@RequestParam Integer channelId) {
        return ResponseDTO.success(metricService.channelMetric(channelId));
    }


    /**
     * 获取通道离线指标
     *
     * @return
     */
    @GetMapping("/metric/offline")
    public ResponseDTO getOfflineMetric(@RequestParam Integer channelId, @RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) {
        return ResponseDTO.success(graphRepresentationService.getOfflineMetric(channelId, startDate, endDate));
    }


    /**
     * 获取通道离线指标
     *
     * @return
     */
    @GetMapping("/metric/offline/refresh")
    public ResponseDTO offlineRefresh(@RequestParam("date") String date) {
        LogUtil.info("开始刷新离线指标，入参：【{}】", date);
        LocalDate refreshDate = LocalDate.parse(date, DateTimeFormatter.ofPattern(DateFormatConstant.YMD)).plusDays(1);
        offlineMetricService.offlineMetric(refreshDate);
        offlineMetricService.report(refreshDate);
//        tpcUploadService.uploadTpcTarget();
        LogUtil.info("开始刷新离线指标刷新完成");
        return ResponseDTO.success();
    }

    /**
     * 确认服务器下线
     *
     * @return
     */
    @GetMapping("/confirm/offline")
    public ResponseDTO confirmOffline(@RequestParam("hostname") String hostname) {
        LogUtil.info("确认服务器下线，入参：【{}】", hostname);
        businessServerInfoApplicationService.confirmOffline(hostname);
        //更新缓存
        cacheService.cacheHostInfo();
        metricService.loadChannelMetricCache();
        LogUtil.info("服务器下线成功");
        return ResponseDTO.success();
    }

    /**
     * 确认服务器下线
     *
     * @return
     */
    @GetMapping("/confirm/online")
    public ResponseDTO confirmOnline(@RequestParam("hostname") String hostname) {
        LogUtil.info("确认服务器上线，入参：【{}】", hostname);
        businessServerInfoApplicationService.confirmOnline(hostname);
        //更新缓存
        cacheService.cacheHostInfo();
        metricService.loadChannelMetricCache();
        LogUtil.info("确认服务器上线成功");
        return ResponseDTO.success();
    }


}
