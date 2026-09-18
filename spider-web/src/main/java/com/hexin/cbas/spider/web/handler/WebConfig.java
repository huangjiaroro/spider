package com.hexin.cbas.spider.web.handler;

import com.hexin.cbas.spider.web.filter.CustomHandlerInterceptor;
import com.hexin.cbas.spider.web.filter.LoginUserHandlerMethodArgumentResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author xuxiang
 * @date 2020/4/17 18:56
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private LoginUserHandlerMethodArgumentResolver loginUserHandlerMethodArgumentResolver;

    @Autowired
    private CustomHandlerInterceptor customHandlerInterceptor;


    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginUserHandlerMethodArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(customHandlerInterceptor).addPathPatterns("/**");
    }

    /**
     * 解决跨域问题
     *
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*")
                .allowedMethods(RequestMethod.POST.name(), RequestMethod.GET.name()
                        , RequestMethod.OPTIONS.name(), RequestMethod.PUT.name()
                        , RequestMethod.DELETE.name())
                .maxAge(3600).allowCredentials(true);
    }
}
