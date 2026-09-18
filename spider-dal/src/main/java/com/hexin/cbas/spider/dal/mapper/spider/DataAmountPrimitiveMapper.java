package com.hexin.cbas.spider.dal.mapper.spider;

import com.hexin.cbas.spider.dal.po.spider.DataAmountPrimitivePO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DataAmountPrimitiveMapper {
    int deleteByPrimaryKey(Long id);

    int insert(DataAmountPrimitivePO record);

    DataAmountPrimitivePO selectByPrimaryKey(Long id);

    int upsert(DataAmountPrimitivePO dataAmountPrimitivePO);

    Long calculateSum(@Param("channelId") Long channelId, @Param("pDate") String pDate);
}