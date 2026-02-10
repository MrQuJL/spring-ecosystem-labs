package com.spring.minimal.common.config;

import com.spring.minimal.common.enums.business.BaseEnum;
import com.spring.minimal.common.validation.EnumValue;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.customizers.PropertyCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Swagger 配置类
 *
 * @author qujianlei
 * @since 1.0.0
 */
@Slf4j
@Configuration
public class SwaggerConfig {

    /**
     * 自定义 Schema 属性，自动将枚举值添加到 description 中
     */
    @Bean
    public PropertyCustomizer enumPropertyCustomizer() {
        return (schema, type) -> {
            if (type.getCtxAnnotations() == null) {
                return schema;
            }

            // 查找 @EnumValue 注解
            EnumValue enumValue = null;
            for (Annotation annotation : type.getCtxAnnotations()) {
                if (annotation instanceof EnumValue) {
                    enumValue = (EnumValue) annotation;
                    break;
                }
            }

            if (enumValue != null) {
                Class<? extends BaseEnum> enumClass = enumValue.enumClass();
                String enumDesc = Arrays.stream(enumClass.getEnumConstants())
                        .map(e -> e.getCode() + "-" + e.getDesc())
                        .collect(Collectors.joining(", "));
                
                String originalDesc = schema.getDescription();
                if (originalDesc == null) {
                    originalDesc = "";
                }
                
                // 避免重复追加
                if (!originalDesc.contains(enumDesc)) {
                    String newDesc = originalDesc + " (" + enumDesc + ")";
                    schema.setDescription(newDesc);
                }
            }
            return schema;
        };
    }
}
