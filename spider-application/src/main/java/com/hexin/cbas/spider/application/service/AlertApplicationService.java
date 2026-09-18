package com.hexin.cbas.spider.application.service;

import com.hexin.cbas.spider.application.assembler.AlertAssembler;
import com.hexin.cbas.spider.core.pojo.AlertMessage;
import com.hexin.cbas.spider.dal.mapper.spider.AlertRecordMapper;
import com.hexin.cbas.spider.dal.po.spider.AlertRecordPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * @description:
 * @author: huangjiarong
 * @create: 2021/10/14
 * @version: v1.0
 */
@Service
public class AlertApplicationService {

    @Autowired
    AlertRecordMapper alertRecordMapper;

    @Autowired
    AlertAssembler alertAssembler;

    @Transactional(rollbackFor = Exception.class)
    public void save(AlertMessage alertMessage) {
        this.save(alertAssembler.toPO(alertMessage));
    }

    @Transactional(rollbackFor = Exception.class)
    public void save(AlertRecordPO alertRecordPO) {
        if (alertRecordPO.getId() == null) {
            alertRecordMapper.insertSelective(alertRecordPO);
        } else {
            alertRecordMapper.updateByPrimaryKeySelective(alertRecordPO);
        }

    }

    @Transactional(rollbackFor = Exception.class)
    public void save(Set<AlertMessage> alertMessages) {
        alertMessages.forEach(this::save);
    }
}
