package com.hexin.cbas.spider.dal.mapper.spider;


import com.hexin.cbas.spider.dal.po.spider.TreeHostInfoPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 业务树信息
 *
 * @author huangjiarong@myhexin.com
 * @date 2024/3/25 15:14
 **/
@Mapper
public interface TreeHostInfoMapper {

    /**
     * 获取所有
     *
     * @author huangjiarong@myhexin.com
     * @date 2024/3/20 16:01
     **/
    List<TreeHostInfoPO> selectAll();

    /**
     * 根据ip获取业务树列表
     *
     * @param ip ip
     * @return List 业务树列表
     * @author huangjiarong@myhexin.com
     * @date 2024/3/20 16:01
     **/
    List<String> businessCode(@Param("ip") String ip);

    /**
     * 确认下线服务器
     *
     * @param hostname 主机名
     * @author huangjiarong@myhexin.com
     * @date 2024/3/20 16:01
     **/
    void confirmOffline(String hostname);

    /**
     * 确认上线服务器
     *
     * @param hostname 主机名
     * @author huangjiarong@myhexin.com
     * @date 2024/3/20 16:01
     **/
    void confirmOnline(String hostname);
}