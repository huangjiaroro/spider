package com.hexin.cbas.spider.dal.query;

import lombok.Data;

import java.util.List;

/**
 * 分页返回实体
 *
 * @author wanghujia
 * @since 2021/8/13
 */
@Data
public class PageResult<T> {

    private List<T> list;

    private Integer count;

    private Integer pages;

    private Integer size;

    private Integer total;
}
