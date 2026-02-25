package com.spring.elastic.module.elastic.dto;

import com.spring.elastic.common.model.req.BasePageReq;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商品分页查询对象
 *
 * @author qujianlei
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "商品分页查询条件")
public class ProductPageQuery extends BasePageReq {

    @Schema(description = "商品名称(模糊搜索)", example = "iPhone")
    private String productName;

    @Schema(description = "分类", example = "手机")
    private String category;

    @Schema(description = "是否上架", example = "true")
    private Boolean isOnSale;

    @Schema(description = "最低价格", example = "1000.00")
    private Double minPrice;

    @Schema(description = "最高价格", example = "5000.00")
    private Double maxPrice;
}
