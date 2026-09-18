package com.hexin.cbas.spider.application.schedule;

import com.alibaba.fastjson.JSON;
import com.hexin.cbas.spider.application.service.BusinessServerInfoApplicationService;
import com.hexin.cbas.spider.config.HostKey;
import com.hexin.cbas.spider.config.SpiderConfig;
import com.hexin.cbas.spider.dal.po.spider.TreeHostInfoPO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @description: 定时缓存服务
 * @author: huangjiarong
 * @create: 2022/5/6
 * @version: v1.0
 */
@Service
@Slf4j
public class CacheService implements InitializingBean {

    @Autowired
    BusinessServerInfoApplicationService businessServerInfoService;

    /**
     * 每10分钟缓存一次
     */
    @Scheduled(cron = "0 */10 * * * ?")
    public void cacheHostInfo() {

        List<TreeHostInfoPO> treeHostInfoPOS = businessServerInfoService.hostInfo();
        Map<String, Set<HostKey>> treeHostCache = new HashMap<>(treeHostInfoPOS.size());

        treeHostInfoPOS.forEach(hostInfo -> {
            treeHostCache.putIfAbsent(hostInfo.getBusinessCode(), new HashSet<>());
            treeHostCache.get(hostInfo.getBusinessCode()).add(HostKey.builder()
                    .exportIp(StringUtils.isEmpty(hostInfo.getExporterIp()) ? hostInfo.getHostName() : hostInfo.getExporterIp())
                    .hostName(hostInfo.getHostName())
                    .inUse(hostInfo.getInUse())
                    .onlineConfirm(hostInfo.getOnlineConfirm()).build());
        });
        log.info("treeHostCache :{}", JSON.toJSONString(treeHostCache));
        SpiderConfig.setArrivalHostCache(treeHostCache);
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        cacheHostInfo();
    }
}
