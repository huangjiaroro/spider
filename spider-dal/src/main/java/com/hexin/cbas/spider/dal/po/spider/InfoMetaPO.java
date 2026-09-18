package com.hexin.cbas.spider.dal.po.spider;

import lombok.Data;

/**
 * @author huangjiarong
 */
@Data
public class InfoMetaPO {
    private Integer id;

    private String infoType;

    private String infoKey;

    private String infoValue;

    private String classify;

    private String placeholder;

    private Integer required;

    private Integer dataType;
}