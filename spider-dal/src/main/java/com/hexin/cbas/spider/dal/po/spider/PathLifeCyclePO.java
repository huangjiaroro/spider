package com.hexin.cbas.spider.dal.po.spider;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Data
@SuppressWarnings("all")
public class PathLifeCyclePO {
    private Integer id;

    private String pathDescription;

    private String path;

    private Integer dayOffset = -8;

    private String dateFormat = "yyyyMMdd";

    private String creator;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    private Boolean status;

    private Integer channelId;

    private Boolean coldBak;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date coldBakStart;

    private Integer coldBakOffset;

    private String updater;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    private String dayUnit = "DAY";

    private String businessLine;

    //分页页码
    private Integer page;
    //分页大小
    private Integer size;
    //偏移量
    private Integer offset;

    public List<String> getPathList() {
        return Arrays.asList(path.split(","));
    }

    public void setPathList(List<String> pathList) {
        this.path = String.join(",", pathList);
    }

    public void initCreate(String currentUser) {
        this.creator = currentUser;
        this.createTime = new Date();
        this.status=true;
        this.coldBak=false;
        this.coldBakOffset=0;
        initUpdate(currentUser);

    }

    public void initUpdate(String currentUser) {
        this.updater = currentUser;
        this.updateTime = new Date();
    }

}