package com.hexin.cbas.spider.dal.mapper.downcount;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @description:
 * @author: huangjiarong
 * @create: 2021/10/20
 * @version: v1.0
 */
@Mapper
public interface DimOthPubDateMapper {

    @Select("SELECT count(1) FROM dim_oth_pub_date WHERE trade_day = #{day}")
    Integer selectBusinessDay(@Param("day") String day);
}
