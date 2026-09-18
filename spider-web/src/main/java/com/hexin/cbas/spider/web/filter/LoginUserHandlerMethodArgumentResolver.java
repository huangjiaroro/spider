package com.hexin.cbas.spider.web.filter;

import com.hexin.cbas.spider.dal.po.metacollection.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

/**
 * @author xuxiang
 * @创建时间 2019/6/24 16:04
 * @描述
 */
@Component
public class LoginUserHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginUserHandlerMethodArgumentResolver.class);

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().isAssignableFrom(User.class) &&
                parameter.hasParameterAnnotation(LoginUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        User user = null;
        try {
            HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
            user = (User) request.getAttribute(TokenAuthFilter.DM_USER);
        } catch (Exception e) {
            LOGGER.error("获取用户信息异常", e);
        }

        return user;
    }
}
