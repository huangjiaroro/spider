package com.hexin.cbas.spider.enums;


import com.hexin.cbas.spider.exception.ExceptionCode;

public enum ErrorMessageEnum implements ExceptionCode {

    ACQUIRE_LOCK_FAIL("10010", "获取分布式锁失败");

    private String code;

    private String info;

    ErrorMessageEnum(String code, String info) {
        this.code = code;
        this.info = info;
    }

    /**
     * 获取异常编码
     *
     * @return 异常码
     */

    @Override
    public String getCode() {
        return code;
    }

    /**
     * 获取异常信息
     *
     * @return 异常信息
     */
    @Override
    public String getMessage() {
        return info;
    }
}
