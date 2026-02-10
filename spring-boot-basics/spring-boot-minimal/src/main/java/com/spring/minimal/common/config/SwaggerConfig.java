package com.spring.minimal.common.config;

import com.spring.minimal.common.enums.business.BaseEnum;
import com.spring.minimal.common.validation.EnumValue;
import io.swagger.v3.oas.models.parameters.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springdoc.core.customizers.PropertyCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
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
     * 自定义 Operation 属性，用于处理 ParameterObject 中的枚举参数
     */
    @Bean
    public OperationCustomizer enumOperationCustomizer() {
        return (operation, handlerMethod) -> {
            if (operation.getParameters() == null) {
                return operation;
            }

            for (Parameter parameter : operation.getParameters()) {
                // 尝试从 HandlerMethod 中找到对应的 DTO 类型
                Class<?>[] parameterTypes = handlerMethod.getMethod().getParameterTypes();
                for (Class<?> paramType : parameterTypes) {
                    // 遍历 DTO 的字段
                    Field field = findField(paramType, parameter.getName());
                    if (field != null) {
                        EnumValue enumValue = field.getAnnotation(EnumValue.class);
                        if (enumValue != null) {
                            appendEnumDesc(parameter, enumValue);
                        }
                    }
                }
            }
            return operation;
        };
    }

    private Field findField(Class<?> clazz, String fieldName) {
        if (clazz == null) {
            return null;
        }
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            return findField(clazz.getSuperclass(), fieldName);
        }
    }

    private void appendEnumDesc(Parameter parameter, EnumValue enumValue) {
        Class<? extends BaseEnum> enumClass = enumValue.enumClass();
        String enumDesc = Arrays.stream(enumClass.getEnumConstants())
                .map(e -> e.getCode() + "-" + e.getDesc())
                .collect(Collectors.joining(", "));
        
        String originalDesc = parameter.getDescription();
        if (originalDesc == null) {
            originalDesc = "";
        }
        
        if (!originalDesc.contains(enumDesc)) {
            String newDesc = originalDesc + " (" + enumDesc + ")";
            parameter.setDescription(newDesc);
            // 兼容 Knife4j：同步修改 Schema 的 description
            if (parameter.getSchema() != null && parameter.getSchema().get$ref() == null) {
                parameter.getSchema().setDescription(newDesc);
            }
        }
    }

    /**
     * 自定义 Schema 属性，自动将枚举值添加到 description 中 (用于 RequestBody)
     */
    @Bean
    public PropertyCustomizer enumPropertyCustomizer() {
        return (schema, type) -> {
            // 获取字段上的注解
            Annotation[] annotations = null;
            if (type.getCtxAnnotations() != null) {
                annotations = type.getCtxAnnotations();
            }
            
            if (annotations == null) {
                return schema;
            }

            // 查找 @EnumValue 注解
            EnumValue enumValue = null;
            for (Annotation annotation : annotations) {
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
