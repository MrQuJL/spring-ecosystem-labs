package com.spring.elastic.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.spring.elastic.common.constants.DateConstants;

import java.time.format.DateTimeFormatter;

/**
 * Spring MVC 配置类
 *
 * @author qujianlei
 * @since 1.0.0
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * 添加自定义的日期时间格式化规则
     * <p>用于配置全局的参数格式化规则（针对 URL 参数和 Form 表单）</p>
     *
     * @param registry 格式化注册器
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {
        DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
        // 配置 LocalDateTime 的格式化规则
        registrar.setDateTimeFormatter(DateTimeFormatter.ofPattern(DateConstants.DATE_TIME_FORMAT));
        // 配置 LocalDate 的格式化规则
        registrar.setDateFormatter(DateTimeFormatter.ofPattern(DateConstants.DATE_FORMAT));
        // 配置 LocalTime 的格式化规则
        registrar.setTimeFormatter(DateTimeFormatter.ofPattern(DateConstants.TIME_FORMAT));
        registrar.registerFormatters(registry);
    }
}
