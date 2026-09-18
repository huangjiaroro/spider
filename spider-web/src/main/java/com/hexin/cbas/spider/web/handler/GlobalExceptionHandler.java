package com.hexin.cbas.spider.web.handler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.hexin.cbas.spider.constants.StatusCodeConstant;
import com.hexin.cbas.spider.exception.BizException;
import com.hexin.cbas.spider.utils.LogUtil;
import com.hexin.cbas.spider.web.dto.ResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author wanghujia
 * @since 2021/8/17
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = BizException.class)
    @ResponseBody
    public ResponseDTO bizExceptionHandler(BizException e) {
        return ResponseDTO.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseDTO handleParameterException(MethodArgumentNotValidException e) {
        String message = e.getMessage();
        if (Objects.nonNull(e.getBindingResult()) && e.getBindingResult().hasErrors()) {
            message = e.getBindingResult().getAllErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(" | "));
        }
        logger.warn(e.getMessage(), e);
        LogUtil.error(e.getMessage(), e);
        return ResponseDTO.error(StatusCodeConstant.PARAM_ERROR, message);
    }

    @ExceptionHandler({InvalidFormatException.class, HttpMessageNotReadableException.class})
    @ResponseBody
    public ResponseDTO handleParameterException(InvalidFormatException e) {
        logger.warn(e.getMessage(), e);
        LogUtil.warn(e.getMessage(), e);
        return ResponseDTO.error(StatusCodeConstant.PARAM_ERROR, "客户端请求的语法错误，请检查传参格式是否有异常");
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseDTO handlerException(Exception e) {
        logger.error(e.getMessage(), e);
        LogUtil.error(e.getMessage(), e);
        return ResponseDTO.error(StatusCodeConstant.INTERNAL_SERVER_ERROR, "服务器内部异常，请联系服务管理员");
    }
}
