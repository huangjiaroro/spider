package com.hexin.cbas.spider.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 边
 * @author: huangjiarong
 * @create: 2021/10/13
 * @version: v1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EdgeDTO {

    private String source;
    private ChannelNodeDTO sourceNode;
    private String target;
    private ChannelNodeDTO targetNode;

    public EdgeDTO(String source, String target) {
        this.source = source;
        this.target = target;
    }

}
