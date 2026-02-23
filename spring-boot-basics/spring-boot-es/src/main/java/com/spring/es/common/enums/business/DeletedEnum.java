package com.spring.es.common.enums.business;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 逻辑删除状态枚举
 * <p>用于标识数据的逻辑删除状态</p>
 *
 * @author qujianlei
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public enum DeletedEnum implements BaseEnum {

    NOT_DELETED(0, "未删除"),
    DELETED(1, "已删除");

    private final Integer code;
    private final String desc;
}
