package com.hexin.cbas.spider.web.controller;

import com.hexin.cbas.spider.application.service.AlertRepresentationService;
import com.hexin.cbas.spider.utils.IpUtil;
import com.hexin.cbas.spider.web.dto.ResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

/**
 * @description:对外接口controller
 * @author: huangjiarong
 * @create: 2021/11/30
 * @version: v1.0
 */
@RestController
@RequestMapping("${api.prefix}/openApi")
public class OpenApiController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OpenApiController.class);

    @Autowired
    AlertRepresentationService service;

    /**
     * 获取运维告警信息
     *
     * @param persons 告警人 多个用英文","分隔
     * @return
     */
    @GetMapping("/alertInfo")
    public ResponseDTO reportDataAmount(@RequestParam("persons") String persons, HttpServletRequest request) {
        String id = UUID.randomUUID().toString();
        LOGGER.info("请求id{},获取运维告警信息 ip{}", id, IpUtil.getRemoteIp(request));
        List<String> result = service.opsAlertInfo(persons);
        LOGGER.info("请求id{},获取运维告警信息 结束 返回数据量{}", id, result.size());
        return ResponseDTO.success(result);
    }
}
