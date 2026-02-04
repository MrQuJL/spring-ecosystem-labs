package com.github.jeremy.springlabs.redis.string.common.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.github.jeremy.springlabs.redis.string.common.interceptor.RequestLogInterceptor;

import java.io.File;

/**
 * WebMvc配置类
 * 用于配置拦截器、资源处理等Web相关配置
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private RequestLogInterceptor requestLogInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 1. 注册请求日志拦截器，对所有请求生效 (建议放在最前面)
        registry.addInterceptor(requestLogInterceptor)
                .addPathPatterns("/**")
                // 排除静态资源请求
                .excludePathPatterns("/static/**", "/error", "/favicon.ico", "/file/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 配置本地上传目录的静态资源映射
        String path = System.getProperty("user.dir") + File.separator + "uploads" + File.separator;
        registry.addResourceHandler("/file/**")
                .addResourceLocations("file:" + path);
    }
}
