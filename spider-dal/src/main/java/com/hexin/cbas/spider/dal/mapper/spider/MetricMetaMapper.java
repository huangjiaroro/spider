package com.hexin.cbas.spider.dal.mapper.spider;

import com.hexin.cbas.spider.dal.po.spider.MetricMetaPO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MetricMetaMapper {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(MetricMetaPO record);

    MetricMetaPO selectByPrimaryKey(Integer id);

    List<MetricMetaPO> selectByType(String metricType);

    int updateByPrimaryKeySelective(MetricMetaPO record);
}