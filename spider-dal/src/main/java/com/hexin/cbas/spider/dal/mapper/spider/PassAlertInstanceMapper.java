package com.hexin.cbas.spider.dal.mapper.spider;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * paas告警实例
 *
 * @author huangjiarong@myhexin.com
 * @date 2024/3/25 15:12
 **/
@Mapper
public interface PassAlertInstanceMapper {

    /**
     * 获取所有异常实例
     *
     * @return java.util.List<java.lang.String>
     * @author huangjiarong@myhexin.com
     * @date 2024/3/25 15:13
     **/
    List<String> selectAll();

    /**
     * 删除异常paas实例
     *
     * @param paasAlertInstance paas实例
     * @author huangjiarong@myhexin.com
     * @date 2024/3/25 15:13
     **/
    void deleteByPrimaryKey(@Param("paasAlertInstance") String paasAlertInstance);

    /**
     * 插入异常的pass告警实例
     *
     * @param paasAlertInstance paas实例
     * @author huangjiarong@myhexin.com
     * @date 2024/3/25 15:14
     **/
    void insert(@Param("paasAlertInstance") String paasAlertInstance);

}