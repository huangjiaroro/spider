package com.hexin.cbas.spider.dal.mapper.spider;

import com.hexin.cbas.spider.dal.po.spider.DataAmountSummaryPO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DataAmountSummaryMapper {
    int insert(DataAmountSummaryPO record);

    int insertSelective(DataAmountSummaryPO record);

    int deleteByPrimaryKey(Long id);

    DataAmountSummaryPO selectByPrimaryKey(Long id);

    int updateByPrimaryKey(DataAmountSummaryPO record);

    int updateByPrimaryKeySelective(DataAmountSummaryPO record);

    int updateSourceAmount(DataAmountSummaryPO dataAmountSummaryPO);

    List<DataAmountSummaryPO> list(@Param("channelId") Long channelId, @Param("startDate") String startDate, @Param("endDate") String endDate);

    DataAmountSummaryPO selectNearestAmount(@Param("channelId") Long channelId, @Param("pDate") String pDate);

    DataAmountSummaryPO selectAmountByChannelIdAndDate(@Param("channelId") Long channelId, @Param("pDate") String pDate);

    Long uploadCount( @Param("pDate") String pDate);

    List<DataAmountSummaryPO> summaryGroup(@Param("startDate") String startDate, @Param("endDate") String endDate);
}