package com.hexin.cbas.spider.dal.po.metacollection;

import lombok.Data;

import java.util.List;

/**
 * @author viruser
 */
@Data
public class User {
    
    private String code;
    private String email;
    private String cert;
    private String allowIps;
    
    private String name;
    private String chineseName;
    
    private List<String> policy;
    private String role;
}
