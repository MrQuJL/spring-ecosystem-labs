package com.spring.mongo.module.mongo.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import com.spring.mongo.common.validation.AddGroup;
import com.spring.mongo.common.validation.UpdateGroup;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 用户数据传输对象
 * <p>用于接收前端传递的用户参数</p>
 *
 * @author qujianlei
 * @since 1.0.0
 */
@Data
@Schema(description = "用户数据传输对象")
public class UserDTO {
    
    @Schema(description = "用户ID", example = "6997e7948b6117674b5d3aa9")
    @Null(message = "新增时ID必须为空", groups = AddGroup.class)
    @NotNull(message = "更新时ID不能为空", groups = UpdateGroup.class)
    private String id;

    @Schema(description = "用户名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    @NotNull(message = "用户名称不能为空")
    private String name;

    @Schema(description = "用户年龄", requiredMode = Schema.RequiredMode.REQUIRED, example = "18")
    @NotNull(message = "用户年龄不能为空")
    private Long age;

    @Schema(description = "用户邮箱", requiredMode = Schema.RequiredMode.REQUIRED, example = "zhangsan@example.com")
    @NotNull(message = "用户邮箱不能为空")
    private String email;
}
