package com.hexin.cbas.spider.application.dto;

import lombok.Data;

/**
 * @author wanghujia
 * @since 2022/1/12
 */
@Data
public class InfoMetaDTO {

    private String key;

    private String value;

    private String placeholder;

    private Integer required;

    private Integer dataType;
}