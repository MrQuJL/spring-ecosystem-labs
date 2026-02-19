package com.spring.rocket.module.customer.enums.response;

import com.spring.rocket.common.enums.response.IResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 客户模块响应提示枚举
 *
 * @author qujianlei
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public enum CustomerResponseEnum implements IResponse {

    CUSTOMER_NOT_EXIST(10001, "客户不存在: %s"),
    STATUS_INVALID(10002, "无效的客户状态: %s"),
    CUSTOMER_NAME_EXIST(10003, "客户名称已存在: %s");

    private final Integer code;
    private final String message;
}
