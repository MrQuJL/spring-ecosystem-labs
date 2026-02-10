package com.spring.minimal.common.validation;

import com.spring.minimal.common.enums.business.BaseEnum;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.Set;

/**
 * 枚举值校验器
 *
 * @author qujianlei
 * @since 1.0.0
 */
public class EnumValueValidator implements ConstraintValidator<EnumValue, Integer> {

    private final Set<Integer> validValues = new HashSet<>();

    @Override
    public void initialize(EnumValue constraintAnnotation) {
        Class<? extends BaseEnum> enumClass = constraintAnnotation.enumClass();
        BaseEnum[] enums = enumClass.getEnumConstants();
        if (enums != null) {
            for (BaseEnum e : enums) {
                validValues.add(e.getCode());
            }
        }
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        // 如果值为 null，则认为验证通过（由 @NotNull 控制非空）
        if (value == null) {
            return true;
        }
        return validValues.contains(value);
    }
}
