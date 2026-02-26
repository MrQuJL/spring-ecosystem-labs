package com.spring.elastic.module.mongo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotNull;

/**
 * 用户状态更新请求对象
 *
 * @author qujianlei
 * @since 1.0.0
 */
@Data
@Schema(description = "用户状态更新请求")
public class UserStatusReq {

    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "6997e7948b6117674b5d3aa9")
    @NotNull(message = "用户ID不能为空")
    private String id;

    @Schema(description = "用户状态(0:正常,1:禁用)", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    @NotNull(message = "用户状态码不能为空")
    private Integer status;
}
