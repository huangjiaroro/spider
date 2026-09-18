package com.hexin.cbas.spider.dal.mapper.spider;

import com.hexin.cbas.spider.dal.po.spider.AlertRecordPO;
import com.hexin.cbas.spider.dal.po.spider.DashboardAlertPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 告警mapper
 *
 * @author huangjiarong@myhexin.com
 * @date 2024/3/25 15:12
 **/
@Mapper
public interface AlertRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AlertRecordPO record);

    int insertSelective(AlertRecordPO record);

    AlertRecordPO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AlertRecordPO record);

    int updateByPrimaryKey(AlertRecordPO record);

    List<AlertRecordPO> selectUnAlertRecord();

    void clearAlertedRecord(@Param("alertDate") Date alertDate);

    List<AlertRecordPO> selectOpsAlertInfo(List<String> list);

    void updateOpsAlertInfo(Integer id);

    List<DashboardAlertPO> dashboardAlert(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    /**
     * 批量更新告警状态
     *
     * @param list id列表
     * @author huangjiarong@myhexin.com
     * @date 2024/3/19 17:37
     **/
    void batchUpdateAlertStatus(List<Integer> list);
}