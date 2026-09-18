package com.hexin.cbas.spider.dal.mapper.spider;

import com.hexin.cbas.spider.dal.po.spider.PathLifeCyclePO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * hdfs路径生命周期
 *
 * @author huangjiarong
 * @version v1.0
 * @date 2024/08/16
 */
@Mapper
public interface PathLifeCycleMapper {

    /**
     * 根据id删除
     *
     * @param id id
     * @return int
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * 插入数据
     *
     * @param record 生命周期po
     * @return int
     */
    int insert(PathLifeCyclePO record);

    /**
     * 选择性插入
     *
     * @param record 生命周期po
     * @return int
     */
    int insertSelective(PathLifeCyclePO record);

    /**
     * 根据id查询
     *
     * @param id id
     * @return PathLifeCyclePO
     */
    PathLifeCyclePO selectByPrimaryKey(Integer id);

    /**
     * 选择性更新
     *
     * @param record 生命周期po
     * @return int
     */
    int updateByPrimaryKeySelective(PathLifeCyclePO record);

    /**
     * 更新
     *
     * @param record 生命周期po
     * @return int
     */
    int updateByPrimaryKey(PathLifeCyclePO record);

    /**
     * 分页查询
     *
     * @param pathLifeCycle 参数
     * @return list
     */
    List<PathLifeCyclePO> pageQuery(PathLifeCyclePO pathLifeCycle);

    /**
     * 分页总数
     *
     * @param pathLifeCycle 参数
     * @return 总条数
     */
    int pageCount(PathLifeCyclePO pathLifeCycle);

    /**
     * 根据通道id获取生命周期
     *
     * @param channelId 通道id
     * @return PathLifeCyclePO
     */
    PathLifeCyclePO selectByChannelId(@Param("channelId") Long channelId);

    /**
     * 根据通道id删除
     *
     * @param channelId 通道id
     */
    void deleteByChannelId(@Param("channelId")Long channelId);
}