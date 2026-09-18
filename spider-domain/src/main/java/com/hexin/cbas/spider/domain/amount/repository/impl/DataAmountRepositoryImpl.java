package com.hexin.cbas.spider.domain.amount.repository.impl;

import com.hexin.cbas.spider.dal.mapper.spider.DataAmountPrimitiveMapper;
import com.hexin.cbas.spider.dal.mapper.spider.DataAmountSummaryMapper;
import com.hexin.cbas.spider.dal.po.spider.DataAmountSummaryPO;
import com.hexin.cbas.spider.domain.amount.convert.DataAmountPrimitiveConvert;
import com.hexin.cbas.spider.domain.amount.convert.DataAmountSummaryConvert;
import com.hexin.cbas.spider.domain.amount.entity.DataAmountPrimitive;
import com.hexin.cbas.spider.domain.amount.entity.DataAmountSummary;
import com.hexin.cbas.spider.domain.amount.repository.DataAmountPrimitiveRepository;
import com.hexin.cbas.spider.domain.amount.repository.DataAmountSummaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author wanghujia
 * @since 2021/8/16
 */
@Repository
public class DataAmountRepositoryImpl implements DataAmountPrimitiveRepository, DataAmountSummaryRepository {

    @Autowired
    private DataAmountPrimitiveMapper dataAmountPrimitiveMapper;

    @Autowired
    private DataAmountPrimitiveConvert dataAmountPrimitiveConvert;

    @Autowired
    private DataAmountSummaryConvert dataAmountSummaryConvert;

    @Autowired
    private DataAmountSummaryMapper dataAmountSummaryMapper;

    @Override
    public void save(DataAmountPrimitive dataAmountPrimitive) {
        // 持久化源端数据量上报信息（无则插入，有则更新）
        dataAmountPrimitiveMapper.upsert(dataAmountPrimitiveConvert.toData(dataAmountPrimitive));
        // 计算源端数据量上报总量（通过for update的悲观锁方式解决相同数据通道，相同日期，不同实例并发上报时引发的小概率并发安全问题）
        // 注意：channel_id 和 p_date 字段必须建立联合索引，否则mysql锁得不是行，而是整张表
//        Long sum = dataAmountPrimitiveMapper.calculateSum(dataAmountPrimitive.getChannelId(), dataAmountPrimitive.getPDate());
//        DataAmountSummaryPO dataAmountSummaryPO = new DataAmountSummaryPO();
//        dataAmountSummaryPO.setChannelId(dataAmountPrimitive.getChannelId());
//        dataAmountSummaryPO.setPDate(dataAmountPrimitive.getPDate());
//        dataAmountSummaryPO.setSourceAmountReport(sum);
//        int updateFlag = dataAmountSummaryMapper.updateSourceAmount(dataAmountSummaryPO);
//        if (updateFlag == 0) {
//            dataAmountSummaryMapper.insertSelective(dataAmountSummaryPO);
//        }
    }

    @Override
    public void remove(Long id) {

    }

    @Override
    public DataAmountPrimitive find(Long id) {
        return null;
    }

    @Override
    public void save(DataAmountSummary dataAmountSummary) {
        DataAmountSummaryPO dataAmountSummaryPO = dataAmountSummaryConvert.toData(dataAmountSummary);
        DataAmountSummaryPO dbAmount = dataAmountSummaryMapper.selectAmountByChannelIdAndDate(dataAmountSummary.getChannelId(), dataAmountSummary.getPDate());
        if (dbAmount == null) {
            dataAmountSummaryMapper.insertSelective(dataAmountSummaryPO);
        } else {
            dataAmountSummaryPO.setId(dbAmount.getId());
            dataAmountSummaryMapper.updateByPrimaryKeySelective(dataAmountSummaryPO);
        }
    }
}
