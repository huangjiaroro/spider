package com.hexin.cbas.spider.application.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hexin.cbas.spider.dal.mapper.spider.ChannelNodeMapper;
import com.hexin.cbas.spider.dal.mapper.spider.PathLifeCycleMapper;
import com.hexin.cbas.spider.dal.po.spider.ChannelNodePO;
import com.hexin.cbas.spider.dal.po.spider.PathLifeCyclePO;
import com.hexin.cbas.spider.dal.query.PageResult;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * hdfs路径生命周期
 *
 * @author huangjiarong
 * @version v1.0
 * @date 2024/08/16
 */
@Service
@SuppressWarnings("all")
public class PathLifeCycleService {

    private final PathLifeCycleMapper pathLifeCycleMapper;
    private final ChannelNodeMapper channelNodeMapper;

    public PathLifeCycleService(PathLifeCycleMapper pathLifeCycleMapper, ChannelNodeMapper channelNodeMapper) {
        this.pathLifeCycleMapper = pathLifeCycleMapper;
        this.channelNodeMapper = channelNodeMapper;
    }

    public PageResult<PathLifeCyclePO> pathLifeCyclePageQuery(PathLifeCyclePO pathLifeCycle) {
        int count = pathLifeCycleMapper.pageCount(pathLifeCycle);
        pathLifeCycle.setOffset((pathLifeCycle.getPage() - 1) * pathLifeCycle.getSize());
        List<PathLifeCyclePO> dataChannelPageList = pathLifeCycleMapper.pageQuery(pathLifeCycle);
        int pages = count % pathLifeCycle.getSize() == 0 ? count / pathLifeCycle.getSize() :
                count / pathLifeCycle.getSize() + 1;
        PageResult<PathLifeCyclePO> pageResult = new PageResult<>();
        pageResult.setList(dataChannelPageList);
        pageResult.setSize(pathLifeCycle.getSize());
        pageResult.setTotal(count);
        pageResult.setPages(pages);
        return pageResult;
    }

    public String startClean(PathLifeCyclePO pathLifeCycle) {
        PathLifeCyclePO update = pathLifeCycleMapper.selectByPrimaryKey(pathLifeCycle.getId());
        update.setStatus(pathLifeCycle.getStatus());
        pathLifeCycleMapper.updateByPrimaryKeySelective(update);
        return "修改成功";
    }

    public PathLifeCyclePO save(PathLifeCyclePO pathLifeCycle) {
        pathLifeCycle = savePathOnly(pathLifeCycle);
        //更新channelNode表
        for (ChannelNodePO node : channelNodeMapper.selectByChannelId(pathLifeCycle.getChannelId())) {
            if ("hdfs".equals(node.getNodeType())) {
                JSONObject jsonObject = JSON.parseObject(node.getMetricInfo());
                jsonObject.put("path", pathLifeCycle.getPath());
                node.setMetricInfo(jsonObject.toJSONString());
                channelNodeMapper.updateByPrimaryKeySelective(node);
            }
        }
        return pathLifeCycle;
    }

    public PathLifeCyclePO savePathOnly(PathLifeCyclePO pathLifeCycle) {
        if (pathLifeCycle.getId() != null) {
            pathLifeCycleMapper.updateByPrimaryKeySelective(pathLifeCycle);
        } else {
            pathLifeCycleMapper.insertSelective(pathLifeCycle);
        }
        return pathLifeCycle;
    }

    public void savePath(Map<String, Object> metricInfos, Long channelId, String currentUser, String bizLine, String name) {
        if (Objects.isNull(metricInfos) || !metricInfos.containsKey("path")) {
            throw new IllegalArgumentException("hdfs 节点路径不能为空");
        }

        PathLifeCyclePO pathLifeCyclePO = pathLifeCycleMapper.selectByChannelId(channelId);
        // 不存在则新增
        if (Objects.isNull(pathLifeCyclePO)) {
            pathLifeCyclePO = new PathLifeCyclePO();
            pathLifeCyclePO.initCreate(currentUser);
        } else {
            pathLifeCyclePO.initUpdate(currentUser);
        }
        pathLifeCyclePO.setBusinessLine(bizLine);
        pathLifeCyclePO.setPathDescription(name);
        pathLifeCyclePO.setChannelId(channelId.intValue());
        pathLifeCyclePO.setPath(metricInfos.get("path").toString());
        savePathOnly(pathLifeCyclePO);
    }

    public void deleteByChannelId(Long channelId) {
        pathLifeCycleMapper.deleteByChannelId(channelId);
    }
}
