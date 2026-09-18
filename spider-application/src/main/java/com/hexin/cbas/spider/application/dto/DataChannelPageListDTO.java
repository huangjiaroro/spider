package com.hexin.cbas.spider.application.dto;

import lombok.Data;

import java.util.Date;

@Data
public class DataChannelPageListDTO {
    private Long id;

    private String name;

    private String bizLine;

    private String state;

    private Boolean health;

    private Double lagTime;

    private Integer metric;

    private String creator;

    private Date createTime;

    private String updater;

    private Date updateTime;
}