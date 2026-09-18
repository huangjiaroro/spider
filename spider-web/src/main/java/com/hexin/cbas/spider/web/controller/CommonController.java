package com.hexin.cbas.spider.web.controller;

import com.hexin.cbas.spider.application.service.CommonApplicationService;
import com.hexin.cbas.spider.application.service.UserService;
import com.hexin.cbas.spider.dal.po.metacollection.User;
import com.hexin.cbas.spider.web.dto.ResponseDTO;
import com.hexin.cbas.spider.web.filter.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wanghujia
 * @since 2021/9/8
 */
@RestController
@RequestMapping("${api.prefix}/common")
public class CommonController {

    @Autowired
    private CommonApplicationService commonApplicationService;

    @Autowired
    private UserService userService;

    @RequestMapping("/getCurrentUser")
    public ResponseDTO getCurrentUser(@LoginUser User user) {
        userService.userPrivilege(user);
        return ResponseDTO.success(user);
    }

    @RequestMapping("/getBizList")
    public ResponseDTO getBizList(@LoginUser User user) {
        return ResponseDTO.success(commonApplicationService.getAllBizNames());
    }
}
