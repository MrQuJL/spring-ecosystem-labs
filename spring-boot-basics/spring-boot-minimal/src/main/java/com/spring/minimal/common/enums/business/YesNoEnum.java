package com.spring.minimal.common.enums.business;

import com.spring.minimal.common.utils.EnumUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 是/否枚举
 * 
 * @author qujianlei
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public enum YesNoEnum implements BaseEnum {

    NO(0, "否"),
    YES(1, "是");

    private final Integer code;
    private final String desc;
    
    /**
     * 根据状态码获取枚举
     * @param code 状态码
     * @return YesNoEnum
     */
    public static YesNoEnum getByCode(Integer code) {
        return EnumUtil.getByCode(YesNoEnum.class, code);
    }
    
    /**
     * 将布尔值转换为枚举
     * @param value 布尔值
     * @return YesNoEnum
     */
    public static YesNoEnum fromBoolean(Boolean value) {
        return value ? YES : NO;
    }
    
    /**
     * 将枚举转换为布尔值
     * @return boolean
     */
    public boolean toBoolean() {
        return this == YES;
    }
}