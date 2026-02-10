package com.spring.minimal.module.customer.dto;

import com.spring.minimal.common.model.req.BasePageReq;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 客户分页查询对象
 *
 * @author spring-minimal
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "客户分页查询条件")
public class CustomerPageQuery extends BasePageReq {

    @Schema(description = "客户名称(模糊搜索)")
    private String name;

    @Schema(description = "客户状态")
    private Integer status;
}
