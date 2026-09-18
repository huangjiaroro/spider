package com.hexin.cbas.spider.dal.query;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 分页查询实体
 *
 * @author wanghujia
 * @since 2021/8/13
 */
@Data
public class PageQuery {

    private Integer offset;

    private Integer limit;

    private String sortField = "id";

    private String sortDirection = "desc";

    private Map<String, Object> queryFields;

    List<String> list;
}
