package com.spring.elastic.module.elastic.enums.response;

import com.spring.elastic.common.enums.response.IResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 商品模块响应提示枚举
 *
 * @author qujianlei
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public enum ProductResponseEnum implements IResponse {

    PRODUCT_NOT_EXIST(20001, "商品不存在: %s"),
    PRODUCT_NAME_EXIST(20002, "商品名称已存在: %s");

    private final Integer code;
    private final String message;
}
