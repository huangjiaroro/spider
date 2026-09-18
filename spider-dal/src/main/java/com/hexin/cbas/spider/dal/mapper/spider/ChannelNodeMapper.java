package com.hexin.cbas.spider.dal.mapper.spider;

import com.hexin.cbas.spider.dal.po.spider.ChannelNodePO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author huangjiarong
 */
@Mapper
public interface ChannelNodeMapper {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(ChannelNodePO record);

    ChannelNodePO selectByPrimaryKey(Integer id);

    ChannelNodePO selectByNodeId(String nodeId);

    List<ChannelNodePO> selectByChannelId(Integer channelId);

    int updateByPrimaryKeySelective(ChannelNodePO record);

    void deleteByChannelId(Long channelId);

    List<String> selectBusinessChannel(List<String> list);
}