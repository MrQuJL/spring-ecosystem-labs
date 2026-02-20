package com.spring.mongo.common.enums.business;

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
}