package com.spring.elastic.module.elastic.dto;

import com.spring.elastic.common.validation.AddGroup;
import com.spring.elastic.common.validation.UpdateGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Data;

/**
 * 商品数据传输对象
 *
 * @author qujianlei
 * @since 1.0.0
 */
@Data
@Schema(description = "商品数据传输对象")
public class ProductDTO {

    @Schema(description = "ES文档ID", example = "uuid-string")
    @Null(message = "新增时ID必须为空", groups = AddGroup.class)
    @NotNull(message = "更新时ID不能为空", groups = UpdateGroup.class)
    private String id;

    @Schema(description = "业务商品ID", example = "1001")
    @NotNull(message = "商品ID不能为空")
    private Long productId;

    @Schema(description = "商品名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "iPhone 15 Pro")
    @NotNull(message = "商品名称不能为空")
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
}
