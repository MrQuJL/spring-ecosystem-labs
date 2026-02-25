package com.spring.elastic.module.elastic.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 商品展示对象
 *
 * @author qujianlei
 * @since 1.0.0
 */
@Data
@Schema(description = "商品展示信息")
public class ProductVO {

    @Schema(description = "ES文档ID", example = "uuid-string")
    private String id;

    @Schema(description = "业务商品ID", example = "1001")
    private Long productId;

    @Schema(description = "商品名称", example = "iPhone 15 Pro")
    private String productName;

    @Schema(description = "分类", example = "手机")
    private String category;

    @Schema(description = "品牌", example = "Apple")
    private String brand;

    @Schema(description = "价格", example = "9999.00")
    private Double price;

    @Schema(description = "库存", example = "100")
    private Integer stock;

    @Schema(description = "是否上架", example = "true")
    private Boolean isOnSale;

    @Schema(description = "创建时间", example = "2023-10-27 10:00:00")
    private LocalDateTime createdAt;
}
