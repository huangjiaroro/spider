package com.hexin.cbas.spider.web.logger;

import ch.qos.logback.classic.pattern.ExtendedThrowableProxyConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.LayoutBase;
import com.alibaba.fastjson.JSON;
import com.hexin.cbas.spider.constants.LogConstant;
import com.hexin.cbas.spider.utils.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

import java.util.Date;

/**
 * 日志打印格式
 *
 * @author wanghujia
 * @since 2021/9/17
 */
public class LogLayout extends LayoutBase<ILoggingEvent> {

    private final static String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";

    @Override
    public String doLayout(ILoggingEvent event) {
        String messageInfo = event.getFormattedMessage();
        if (event.getThrowableProxy() != null) {
            //打印堆栈信息
            ExtendedThrowableProxyConverter converter = new ExtendedThrowableProxyConverter();
            converter.start();
            messageInfo = event.getFormattedMessage() + CoreConstants.LINE_SEPARATOR + converter.convert(event);
            if (StringUtils.contains(messageInfo, "@")) {
                messageInfo = StringUtils.replace(messageInfo, "@", "");
            }
            converter.stop();
        }
        LogMessage message = LogMessage.builder()
                .time(event.getTimeStamp())
                .logtime(DateUtil.format(new Date(event.getTimeStamp()), DATE_PATTERN))
                .traceId(MDC.get(LogConstant.TRACE_ID))
                .uri(MDC.get(LogConstant.URI))
                .loginUser(MDC.get(LogConstant.LOGIN_USER))
                .cost(System.currentTimeMillis() - Long.parseLong(MDC.get(LogConstant.BEGIN_TIME)))
                .level(event.getLevel().levelStr)
                .message(messageInfo)
                .build();

        return String.format("%s%s", JSON.toJSONString(message), CoreConstants.LINE_SEPARATOR);
    }
}
