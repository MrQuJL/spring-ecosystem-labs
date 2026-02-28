package com.spring.elastic.module.mongo.enums.response;

import com.spring.elastic.common.enums.response.IResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户模块响应提示枚举
 *
 * @author qujianlei
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public enum UserResponseEnum implements IResponse {

    USER_NOT_EXIST(10001, "用户不存在: %s"),
    USER_STATUS_INVALID(10002, "无效的用户状态: %s"),
    USER_NAME_EXIST(10003, "用户名称已存在: %s"),
    USER_EMAIL_EXIST(10004, "用户邮箱已存在: %s");

    private final Integer code;
    private final String message;
}
