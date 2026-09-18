package com.hexin.cbas.spider.application.service;

import com.hexin.cbas.spider.application.dto.DataChannelPageListDTO;
import com.hexin.cbas.spider.dal.query.PageQuery;
import com.hexin.cbas.spider.dal.query.PageResult;
import com.hexin.cbas.spider.web.access.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: huangjiarong
 * @create: 2022/5/6
 * @version: v1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class DataChannelRepresentationServiceTest {
    @Autowired
    DataChannelRepresentationService service;

    @Test
    public void pageQuery() {
        PageQuery pageQuery = new PageQuery();
        pageQuery.setOffset(0);
        pageQuery.setLimit(10);
        Map<String, Object> map = new HashMap<>();
        map.put("keyword", "172.21.54.149");
        pageQuery.setQueryFields(map);
        PageResult<DataChannelPageListDTO> dataChannelPageListDTOPageResult = service.pageQuery(pageQuery);
        System.out.println(dataChannelPageListDTOPageResult);
    }
}