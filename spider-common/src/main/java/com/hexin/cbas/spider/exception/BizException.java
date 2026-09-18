package com.hexin.cbas.spider.exception;

import lombok.Getter;

/**
 * @author wanghujia
 * @since 2021/8/17
 */
@Getter
public class BizException extends RuntimeException {

    private String code;

    private String message;

    public BizException(ExceptionCode exceptionCode) {
        super(exceptionCode.getMessage());
        this.code = exceptionCode.getCode();
        this.message = exceptionCode.getMessage();
    }

    public BizException(String exceptionCode) {
        super(exceptionCode);
    }

}
