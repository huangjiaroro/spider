package com.hexin.cbas.spider.utils;

import com.hexin.cbas.spider.constants.LogConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author viruser
 */
public class LogUtil {

    private static final Logger USER_INSIGHT_LOG = LoggerFactory.getLogger(LogConstant.SPIDER_LOG);

    public static void info(String msg) {
        USER_INSIGHT_LOG.info(msg);
    }


    public static void info(String msg, Exception e) {
        USER_INSIGHT_LOG.info(msg, e);
    }

    public static void info(String format, Object... args) {
        USER_INSIGHT_LOG.info(format, args);
    }

    public static void warn(String msg) {
        USER_INSIGHT_LOG.warn(msg);
    }

    public static void warn(String msg, Exception e) {
        USER_INSIGHT_LOG.warn(msg, e);
    }

    public static void warn(String format, Object... args) {
        USER_INSIGHT_LOG.warn(format, args);
    }

    public static void error(String msg) {
        USER_INSIGHT_LOG.error(msg);
    }

    public static void error(String msg, Exception e) {
        USER_INSIGHT_LOG.error(msg, e);
    }

    public static void error(String format, Object... args) {
        USER_INSIGHT_LOG.error(format, args);
    }


}
