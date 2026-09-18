package com.hexin.cbas.spider.domain.channel.service;

import com.hexin.cbas.spider.domain.channel.entity.DataChannel;
import com.hexin.cbas.spider.domain.channel.repository.DataChannelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wanghujia
 * @since 2021/8/9
 */
@Service
public class DataChannelDomainService {

    @Autowired
    private DataChannelRepository dataChannelRepository;

    public void delete(Long id) {
        dataChannelRepository.remove(id);
    }

    public void save(DataChannel dataChannel) {
        dataChannelRepository.save(dataChannel);
    }

    public DataChannel getById(Long id) {
        return dataChannelRepository.find(id);
    }

    public void changeState(Long id, String state) {
        dataChannelRepository.changeState(id, state);
    }

    public void changeMetric(Long id, Integer metric) {
        dataChannelRepository.changeMetric(id, metric);
    }
}
