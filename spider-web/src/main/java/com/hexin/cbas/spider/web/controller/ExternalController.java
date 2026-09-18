package com.hexin.cbas.spider.web.controller;

import com.hexin.cbas.spider.application.dto.DataAmountPrimitiveDTO;
import com.hexin.cbas.spider.application.service.DataAmountApplicationService;
import com.hexin.cbas.spider.web.dto.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wanghujia
 * @since 2021/8/3
 */
@RestController
@RequestMapping("${api.prefix}/external")
public class ExternalController {

    @Autowired
    private DataAmountApplicationService dataAmountApplicationService;

    @PostMapping("/report_data_amount")
    public ResponseDTO reportDataAmount(@Validated @RequestBody DataAmountPrimitiveDTO dataAmountPrimitiveDTO) {
        dataAmountApplicationService.add(dataAmountPrimitiveDTO);
        return ResponseDTO.success();
    }
}
