package com.hexin.cbas.spider.config;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 服务器Key
 *
 * @author huangjiarong@myhexin.com
 * @date 2024/3/18 21:40
 */
@Data
@Builder
@EqualsAndHashCode
public class HostKey implements Comparable<HostKey> {
    private static final Logger LOGGER = LoggerFactory.getLogger(HostKey.class);
    private String hostName;
    private String exportIp;
    private Boolean inUse;
    private Boolean onlineConfirm;


    @Override
    public int compareTo(HostKey hostKey) {
        try {
            return Integer.valueOf(this.exportIp).compareTo(Integer.valueOf(hostKey.getExportIp()));
        } catch (Exception e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("", e);
            }
        }
        return hostKey.getExportIp().compareTo(this.exportIp);
    }
}
