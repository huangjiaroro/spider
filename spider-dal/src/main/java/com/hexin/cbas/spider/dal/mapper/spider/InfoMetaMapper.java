package com.hexin.cbas.spider.dal.mapper.spider;

import com.hexin.cbas.spider.dal.po.spider.InfoMetaPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface InfoMetaMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(InfoMetaPO record);

    int insertSelective(InfoMetaPO record);

    InfoMetaPO selectByPrimaryKey(Integer id);

    List<InfoMetaPO> selectByTypeAndClassify(@Param("infoType") String infoType, @Param("classify") Integer classify);

    int updateByPrimaryKeySelective(InfoMetaPO record);

    int updateByPrimaryKey(InfoMetaPO record);
}