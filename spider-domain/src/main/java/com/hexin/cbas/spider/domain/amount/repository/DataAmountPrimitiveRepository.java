package com.hexin.cbas.spider.domain.amount.repository;

import com.hexin.cbas.spider.domain.amount.entity.DataAmountPrimitive;

/**
 * @author wanghujia
 * @since 2021/8/16
 */
public interface DataAmountPrimitiveRepository {

    void save(DataAmountPrimitive dataAmountPrimitive);

    void remove(Long id);

    DataAmountPrimitive find(Long id);
}
