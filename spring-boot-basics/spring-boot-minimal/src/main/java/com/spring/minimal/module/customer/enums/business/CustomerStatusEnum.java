package com.spring.minimal.module.customer.enums.business;

import com.spring.minimal.common.enums.business.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 客户状态枚举
 *
 * @author qujianlei
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public enum CustomerStatusEnum implements BaseEnum {

    FROZEN(0, "冻结"),
    NORMAL(1, "正常");

    private final Integer code;
    private final String desc;
}
