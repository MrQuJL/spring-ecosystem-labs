package com.spring.mongo.module.customer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 客户状态更新请求对象
 *
 * @author qujianlei
 * @since 1.0.0
 */
@Data
@Schema(description = "客户状态更新请求")
public class CustomerStatusReq {

    @Schema(description = "客户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1001")
    @NotNull(message = "客户ID不能为空")
    private Long id;

    @Schema(description = "客户状态(0:正常,1:禁用)", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    @NotNull(message = "客户状态码不能为空")
    private Integer status;
}
