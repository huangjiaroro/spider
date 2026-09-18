package com.hexin.cbas.spider.domain.channel.repository;

import com.hexin.cbas.spider.domain.channel.entity.DataChannel;

/**
 * @author wanghujia
 * @since 2021/8/11
 */
public interface DataChannelRepository {

    void save(DataChannel dataChannel);

    void remove(Long id);

    DataChannel find(Long id);

    void changeState(Long id, String state);

    void changeMetric(Long id, Integer metric);
}
