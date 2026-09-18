package com.hexin.cbas.spider.plugin.util;

import com.hexin.cbas.spider.core.pojo.IResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

/**
 * 重试工具类
 *
 * @author : huangjiarong
 * @version: v1.0
 */
public final class RetryUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(RetryUtil.class);

    private static final Integer DEFAULT_MAX_RETRIES = 3;
    private static final Long DEFAULT_DELAY_MILLIS = 50L;

    private RetryUtil() {
    }

    /**
     * 重试某个操作，直到成功或达到最大重试次数
     *
     * @param operation 需要重试的操作
     * @return IResponse
     */
    public static IResponse retry(Supplier<IResponse> operation) {
        return retry(DEFAULT_MAX_RETRIES, DEFAULT_DELAY_MILLIS, operation);
    }


    /**
     * 重试某个操作，直到成功或达到最大重试次数
     *
     * @param maxRetries  最大重试次数
     * @param delayMillis 每次重试之间的延迟时间（毫秒）
     * @param operation   需要重试的操作
     * @return IResponse
     */
    public static IResponse retry(int maxRetries, long delayMillis, Supplier<IResponse> operation) {

        IResponse response = null;
        for (int attempt = 0; attempt < maxRetries; attempt++) {
            try {
                response = operation.get();
                if (response.isSuccess()) {
                    return response;
                }
            } catch (Exception e) {
                // 捕获异常并继续重试
                LOGGER.error("Attempt {}  failed with exception:", attempt + 1, e);
            }
            try {
                Thread.sleep(delayMillis);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return response;
    }
}
