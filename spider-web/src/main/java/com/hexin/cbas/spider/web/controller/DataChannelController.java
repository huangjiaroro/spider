package com.hexin.cbas.spider.web.controller;

import com.alibaba.fastjson.JSON;
import com.hexin.cbas.spider.application.dto.DataChannelDTO;
import com.hexin.cbas.spider.application.schedule.GraphMetricService;
import com.hexin.cbas.spider.application.service.DataChannelApplicationService;
import com.hexin.cbas.spider.application.service.DataChannelRepresentationService;
import com.hexin.cbas.spider.dal.query.PageQuery;
import com.hexin.cbas.spider.utils.LogUtil;
import com.hexin.cbas.spider.web.dto.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author wanghujia
 * @since 2021/8/3
 */
@RestController
@RequestMapping("${api.prefix}/dataChannel")
public class DataChannelController {

    @Autowired
    private DataChannelApplicationService dataChannelApplicationService;

    @Autowired
    private DataChannelRepresentationService dataChannelRepresentationService;

    @Autowired
    GraphMetricService metricService;

    @PostMapping("/save")
    public ResponseDTO save(@RequestBody DataChannelDTO dataChannelDTO) {
        LogUtil.info("保存数据通道开始，入参：【{}】", JSON.toJSONString(dataChannelDTO));
        dataChannelApplicationService.save(dataChannelDTO);
        //更新缓存
        metricService.loadChannelMetricCache();
        LogUtil.info("增加数据通道结束");
        return ResponseDTO.success();
    }

    @PostMapping("/delete")
    @Deprecated
    public ResponseDTO delete(@RequestBody DataChannelDTO dataChannelDTO) {
        LogUtil.info("删除数据通道开始，入参：【{}】", JSON.toJSONString(dataChannelDTO));
        dataChannelApplicationService.delete(dataChannelDTO);
        LogUtil.info("删除数据通道结束");
        return ResponseDTO.success();
    }

    @PostMapping("/changeState")
    public ResponseDTO changeState(@RequestBody DataChannelDTO dataChannelDTO) {
        LogUtil.info("修改数据通道状态开始，入参：【{}】", JSON.toJSONString(dataChannelDTO));
        dataChannelApplicationService.changeState(dataChannelDTO);
        LogUtil.info("修改数据通道结束");
        return ResponseDTO.success();
    }

    @PostMapping("/changeMetric")
    public ResponseDTO changeMetric(@RequestBody DataChannelDTO dataChannelDTO) {
        LogUtil.info("修改监控开关开始，入参：【{}】", JSON.toJSONString(dataChannelDTO));
        dataChannelApplicationService.changeMetric(dataChannelDTO);
        //更新缓存
        metricService.loadChannelMetricCache();
        LogUtil.info("修改监控开关结束");
        return ResponseDTO.success();
    }

    @PostMapping("/pageList")
    public ResponseDTO pageList(@RequestBody PageQuery pageQuery) {
        return ResponseDTO.success(dataChannelRepresentationService.pageQuery(pageQuery));
    }

    @GetMapping("/detail")
    public ResponseDTO get(@RequestParam("channelId") Long id) {
        return ResponseDTO.success(dataChannelApplicationService.get(id));
    }
}
