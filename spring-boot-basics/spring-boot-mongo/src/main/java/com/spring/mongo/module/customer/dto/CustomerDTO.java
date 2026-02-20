package com.spring.mongo.module.customer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import com.spring.mongo.common.validation.AddGroup;
import com.spring.mongo.common.validation.UpdateGroup;

/**
 * 客户数据传输对象
 * <p>用于接收前端传递的客户参数</p>
 *
 * @author qujianlei
 * @since 1.0.0
 */
@Data
@Schema(description = "客户数据传输对象")
public class CustomerDTO {

    /**
     * 客户ID
     */
    @Schema(description = "客户ID", example = "1")
    @Null(message = "新增时ID必须为空", groups = AddGroup.class)
    @NotNull(message = "更新时ID不能为空", groups = UpdateGroup.class)
    private Long id;

    /**
     * 客户名称
     */
    @Schema(description = "客户名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    @NotNull(message = "客户名称不能为空")
    private String name;
}
