package com.spring.rocket.common.enums.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 系统提示性枚举
 * 
 * @author qujianlei
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public enum ResponseEnum implements IResponse {
    
    SUCCESS(200, "success"),
    PARAM_ERROR(400, "参数错误"),
    UNAUTHORIZED(401, "未授权"),
    FORBIDDEN(403, "权限不足"),
    NOT_FOUND(404, "未找到资源"),
    SYSTEM_ERROR(500, "系统错误"),
    BUSINESS_ERROR(600, "业务异常");
    
    private final Integer code;
    private final String message;
}