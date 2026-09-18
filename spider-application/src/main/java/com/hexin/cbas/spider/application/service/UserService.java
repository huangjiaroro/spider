package com.hexin.cbas.spider.application.service;

import com.hexin.cbas.spider.dal.po.metacollection.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @author chenkai
 * @date 2021/9/1 10:47
 **/
@Service
public class UserService {

    @Value("${spider_admin}")
    private Set<String> adminSet;


    public void userPrivilege(User user) {
        if (adminSet.contains(user.getEmail())) {
            user.setRole("ADMIN");
        } else {
            user.setRole("GUEST");
        }
    }
}
