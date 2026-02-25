package com.spring.elastic.module.customer.dto;

import com.spring.elastic.common.model.req.BasePageReq;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 客户分页查询对象
 *
 * @author qujianlei
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "客户分页查询条件")
public class CustomerPageQuery extends BasePageReq {

    @Schema(description = "客户名称(模糊搜索)", example = "张三")
    private String name;

    @Schema(description = "客户状态(0:正常,1:禁用)", example = "0")
    private Integer status;
}
