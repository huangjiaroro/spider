package com.hexin.cbas.spider.dal.mapper.spider;

import com.hexin.cbas.spider.dal.po.spider.ReportPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 指标mapper
 *
 * @author huangjiarong@myhexin.com
 * @date 2023/9/21 20:00
 **/
@Mapper
public interface ReportMapper {

    /**
     * select by p_date
     *
     * @param pDate
     * @return com.hexin.cbas.spider.dal.po.spider.ReportPO
     * @author huangjiarong@myhexin.com
     * @date 2023/9/21 20:35
     **/
    ReportPO selectByDate(@Param("pDate") String pDate);

    /**
     * insert data
     *
     * @param reportPO
     * @author huangjiarong@myhexin.com
     * @date 2023/9/21 20:36
     **/
    void insert(ReportPO reportPO);

    /**
     * delete data
     *
     * @param pDate
     * @author huangjiarong@myhexin.com
     * @date 2023/9/21 20:36
     **/
    void deleteByPrimaryDate(@Param("pDate") String pDate);

    /**
     * 生成某天的看板指标
     * @author huangjiarong@myhexin.com
     * @date 2023/9/22 14:22
     * @param pDate
     * @return com.hexin.cbas.spider.dal.po.spider.ReportPO
     **/
    ReportPO report(@Param("pDate") String pDate);

}