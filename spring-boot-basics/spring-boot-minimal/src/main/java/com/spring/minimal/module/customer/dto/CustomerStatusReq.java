package com.spring.minimal.module.customer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 客户状态更新请求对象
 * <p>企业级开发规范：
 * 1. 使用专用 Request/DTO 对象代替散乱的 @RequestParam
 * 2. 方便后续扩展（比如增加“变更原因”、“操作人”等字段）
 * 3. 统一使用 JSON 格式交互
 * </p>
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

    @Schema(description = "目标状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "状态码不能为空")
    private Integer status;
}
