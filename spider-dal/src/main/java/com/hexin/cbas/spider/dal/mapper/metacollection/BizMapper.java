package com.hexin.cbas.spider.dal.mapper.metacollection;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * created by chenrupeng on 2020/10/21
 */
@Mapper
public interface BizMapper {
    @Select("select chinese_name from m_business")
    List<String> getAllBizNames();
}