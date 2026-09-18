package com.hexin.cbas.spider.dal.mapper.spider;

import com.hexin.cbas.spider.dal.po.spider.DataChannelPO;
import com.hexin.cbas.spider.dal.query.PageQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DataChannelMapper {
    int insert(DataChannelPO record);

    int deleteByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DataChannelPO record);

    DataChannelPO selectByPrimaryKey(Long id);

    List<DataChannelPO>  selectRunningChannel();

    List<DataChannelPO> pageQuery(PageQuery pageQuery);

    int pageCount(PageQuery pageQuery);

    void changeState(@Param("id") Long id, @Param("state") String state);

    void changeMetric(@Param("id") Long id, @Param("metric") Integer metric);
}