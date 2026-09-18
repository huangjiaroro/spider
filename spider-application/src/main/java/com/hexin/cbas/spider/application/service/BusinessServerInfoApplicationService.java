package com.hexin.cbas.spider.application.service;

import com.hexin.cbas.spider.dal.mapper.spider.TreeHostInfoMapper;
import com.hexin.cbas.spider.dal.po.spider.TreeHostInfoPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description:
 * @author: huangjiarong
 * @create: 2022/5/6
 * @version: v1.0
 */
@Service
public class BusinessServerInfoApplicationService {

    private final TreeHostInfoMapper mapper;

    public BusinessServerInfoApplicationService(TreeHostInfoMapper mapper) {
        this.mapper = mapper;
    }

    public List<TreeHostInfoPO> hostInfo() {
        return mapper.selectAll();
    }

    public List<String> businessCode(String ip) {
        return mapper.businessCode(ip);
    }

    public void confirmOffline(String hostname) {
        mapper.confirmOffline(hostname);
    }

    public void confirmOnline(String hostname) {
        mapper.confirmOnline(hostname);
    }
}
