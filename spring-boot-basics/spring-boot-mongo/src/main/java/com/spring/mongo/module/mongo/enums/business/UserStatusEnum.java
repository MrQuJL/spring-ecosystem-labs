package com.spring.mongo.module.mongo.enums.business;

import com.spring.mongo.common.enums.business.BaseEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户状态枚举
 *
 * @author qujianlei
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public enum UserStatusEnum implements BaseEnum {

    FROZEN(0, "冻结"),
    NORMAL(1, "正常");

    private final Integer code;
    private final String desc;
}
