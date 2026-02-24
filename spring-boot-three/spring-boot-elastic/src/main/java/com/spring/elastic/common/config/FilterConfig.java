package com.spring.elastic.common.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Collections;

/**
 * 过滤器统一配置类
 * <p>用于集中管理系统中的所有过滤器及其执行顺序</p>
 *
 * @author qujianlei
 * @since 1.0.0
 */
@Configuration
public class FilterConfig {

    /**
     * 跨域过滤器
     * <p>解决前后端分离架构下的跨域请求问题</p>
     * <p>执行顺序：最高优先级 (Ordered.HIGHEST_PRECEDENCE)</p>
     */
    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        // 允许所有域名进行跨域调用
        config.setAllowedOriginPatterns(Collections.singletonList("*"));
        // 允许跨域发送cookie
        config.setAllowCredentials(true);
        // 允许所有请求方法：GET、POST、PUT、DELETE 等
        config.addAllowedMethod("*");
        // 允许所有请求头
        config.addAllowedHeader("*");
        // 暴露哪些头部信息（可根据需要添加）
        config.addExposedHeader("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 对所有路径应用该跨域配置
        source.registerCorsConfiguration("/**", config);

        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
        // 设置优先级为最高，确保跨域配置最先生效
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }
}
