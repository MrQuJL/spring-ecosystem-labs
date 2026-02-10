package com.spring.minimal.common.validation;

import com.spring.minimal.common.enums.business.BaseEnum;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 校验值是否在指定枚举范围内
 *
 * @author spring-minimal
 * @since 1.0.0
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = EnumValueValidator.class)
public @interface EnumValue {

    /**
     * 错误提示
     */
    String message() default "参数值不在合法范围内";

    /**
     * 分组
     */
    Class<?>[] groups() default {};

    /**
     * 负载
     */
    Class<? extends Payload>[] payload() default {};

    /**
     * 指定枚举类，必须实现 BaseEnum 接口
     */
    Class<? extends BaseEnum> enumClass();
}
