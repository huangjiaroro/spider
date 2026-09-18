package com.hexin.cbas.spider.web.controller;

import com.hexin.cbas.spider.application.service.PathLifeCycleService;
import com.hexin.cbas.spider.dal.po.spider.PathLifeCyclePO;
import com.hexin.cbas.spider.web.dto.ResponseDTO;
import org.springframework.web.bind.annotation.*;

/**
 * hdfs路径生命周期
 *
 * @author huangjiarong
 * @version v1.0
 * @date 2024/08/16
 */
@RestController
@RequestMapping("${api.prefix}/path_life")
public class PathLifeCycleController {


    private final PathLifeCycleService pathLifeCycleService;

    public PathLifeCycleController(PathLifeCycleService pathLifeCycleService) {
        this.pathLifeCycleService = pathLifeCycleService;
    }

    @GetMapping("/page_query")
    public ResponseDTO pathLifeCyclePageQuery(PathLifeCyclePO pathLifeCycle) {
        return ResponseDTO.success(pathLifeCycleService.pathLifeCyclePageQuery(pathLifeCycle));
    }

    @PostMapping("/start_clean")
    public ResponseDTO startClean(@RequestBody PathLifeCyclePO pathLifeCycle) {
        return ResponseDTO.success(pathLifeCycleService.startClean(pathLifeCycle));
    }

    @PostMapping("/save")
    public ResponseDTO save(@RequestBody PathLifeCyclePO pathLifeCycle) {
        return ResponseDTO.success(pathLifeCycleService.save(pathLifeCycle));
    }


}
