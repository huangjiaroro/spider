package com.hexin.cbas.spider.web.controller;

import com.hexin.cbas.spider.web.dto.ResponseDTO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wanghujia
 * @since 2021/8/18
 */
@RestController
public class HeartCheckController {

    @RequestMapping("/readiness")
    public ResponseDTO readiness() {
        return ResponseDTO.success();
    }
}
