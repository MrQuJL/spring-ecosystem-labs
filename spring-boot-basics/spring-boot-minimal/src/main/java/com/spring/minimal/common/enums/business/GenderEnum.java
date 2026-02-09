package com.spring.minimal.common.enums.business;

import com.spring.minimal.common.utils.EnumUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 性别枚举
 * 
 * @author qujianlei
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public enum GenderEnum implements BaseEnum {

    UNKNOWN(0, "未知"),
    MALE(1, "男"),
    FEMALE(2, "女");

    private final Integer code;
    private final String desc;
    
    /**
     * 根据性别码获取枚举
     * @param code 性别码
     * @return GenderEnum
     */
    public static GenderEnum getByCode(Integer code) {
        return EnumUtil.getByCode(GenderEnum.class, code, UNKNOWN);
    }
}