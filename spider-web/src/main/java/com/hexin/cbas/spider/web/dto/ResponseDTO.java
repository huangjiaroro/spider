package com.hexin.cbas.spider.web.dto;

import com.hexin.cbas.spider.constants.StatusCodeConstant;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class ResponseDTO implements Serializable {
    private String code;
    private String msg;
    private Object data;

    public static ResponseDTO success() {
        return new ResponseDTO(StatusCodeConstant.OK, null, null);
    }

    public static ResponseDTO success(Object data) {
        return new ResponseDTO(StatusCodeConstant.OK, null, data);
    }

    public static ResponseDTO error(String code, String msg) {
        return new ResponseDTO(code, msg, null);
    }
}