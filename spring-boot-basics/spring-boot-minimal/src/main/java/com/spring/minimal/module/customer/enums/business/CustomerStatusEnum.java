package com.spring.minimal.module.customer.enums.business;

import com.spring.minimal.common.enums.business.BaseEnum;
import com.spring.minimal.common.utils.EnumUtil;
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
    NORMAL(1, "正常"),
    TEST(2, "测试");

    private final Integer code;
    private final String desc;

    /**
     * 根据状态码获取枚举
     *
     * @param code 状态码
     * @return CustomerStatusEnum
     */
    public static CustomerStatusEnum getByCode(Integer code) {
        return EnumUtil.getByCode(CustomerStatusEnum.class, code, FROZEN);
    }
}
