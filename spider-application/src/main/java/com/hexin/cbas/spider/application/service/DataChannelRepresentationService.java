package com.hexin.cbas.spider.application.service;

import com.hexin.cbas.spider.application.assembler.DataChannelPageListAssembler;
import com.hexin.cbas.spider.application.dto.DataChannelPageListDTO;
import com.hexin.cbas.spider.dal.mapper.spider.DataChannelMapper;
import com.hexin.cbas.spider.dal.po.spider.DataChannelPO;
import com.hexin.cbas.spider.dal.query.PageQuery;
import com.hexin.cbas.spider.dal.query.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author wanghujia
 * @since 2021/8/11
 */
@Service
public class DataChannelRepresentationService {

    @Autowired
    private DataChannelMapper dataChannelMapper;

    @Autowired
    private DataChannelPageListAssembler dataChannelPageListAssembler;

    @Autowired
    BusinessServerInfoApplicationService businessServerInfoApplicationService;

    @Autowired
    DataGraphApplicationService graphApplicationService;

    public PageResult<DataChannelPageListDTO> pageQuery(PageQuery pageQuery) {
        //将ip转为业务树
        if (Objects.nonNull(pageQuery.getQueryFields().get("keyword"))) {
            List<String> businessCodes = businessServerInfoApplicationService.businessCode((String) pageQuery.getQueryFields().get("keyword"));
            if (businessCodes.size() > 0) {
                List<String> businessChannel = graphApplicationService.selectBusinessChannel(businessCodes);
                if (businessChannel.size() > 0) {
                    pageQuery.getQueryFields().put("keyword", "");
                    pageQuery.setList(businessChannel);
                }
            }

        }

        List<DataChannelPageListDTO> dataChannelPageList = dataChannelMapper.pageQuery(pageQuery)
                .stream()
                .map(e -> dataChannelPageListAssembler.toDTO(e))
                .collect(Collectors.toList());
        int count = dataChannelMapper.pageCount(pageQuery);

        PageResult<DataChannelPageListDTO> pageResult = new PageResult<>();
        pageResult.setList(dataChannelPageList);
        pageResult.setCount(count);
        return pageResult;
    }


    public List<DataChannelPO> runningChannel() {
        return dataChannelMapper.selectRunningChannel();
    }
}
