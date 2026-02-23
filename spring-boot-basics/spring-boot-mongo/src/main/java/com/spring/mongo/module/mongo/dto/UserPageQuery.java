package com.spring.mongo.module.mongo.dto;

import com.spring.mongo.common.model.req.BasePageReq;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户分页查询对象
 *
 * @author qujianlei
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "用户分页查询条件")
public class UserPageQuery extends BasePageReq {

    @Schema(description = "用户名称(模糊搜索)", example = "张三")
    private String name;

    @Schema(description = "用户邮箱(模糊搜索)", example = "zhangsan@example.com")
    private String email;

    @Schema(description = "用户状态(0:正常,1:禁用)", example = "0")
    private Integer status;
}
