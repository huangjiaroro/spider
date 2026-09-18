package com.hexin.cbas.spider.dal.mapper.spider;

import com.hexin.cbas.spider.dal.po.spider.MetricRecordPO;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;
/**
 * 实时指标监控
 * @author huangjiarong@myhexin.com      
 * @date 2023/9/22 15:26
 **/
public interface MetricRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MetricRecordPO record);

    int insertSelective(MetricRecordPO record);

    MetricRecordPO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MetricRecordPO record);

    int updateByPrimaryKey(MetricRecordPO record);

    List<MetricRecordPO> healthGroup(@Param("startDate")String startDate, @Param("endDate")String endDate);

    List<MetricRecordPO> lagTimeGroup(@Param("startDate")String startDate,@Param("endDate") String endDate);

    /**
     * 清除指标日志
     * @author huangjiarong@myhexin.com
     * @date 2023/9/22 15:04
     * @param metricDate
     **/
    void clearMetricRecord(@Param("metricDate") LocalDate metricDate);
}