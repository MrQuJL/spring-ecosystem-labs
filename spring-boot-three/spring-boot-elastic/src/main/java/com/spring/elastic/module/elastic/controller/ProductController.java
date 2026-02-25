package com.spring.elastic.module.elastic.controller;

import com.spring.elastic.common.result.Result;
import com.spring.elastic.common.validation.AddGroup;
import com.spring.elastic.common.validation.UpdateGroup;
import com.spring.elastic.module.elastic.dto.ProductDTO;
import com.spring.elastic.module.elastic.dto.ProductPageQuery;
import com.spring.elastic.module.elastic.service.IProductService;
import com.spring.elastic.module.elastic.vo.ProductVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品API接口
 *
 * @author qujianlei
 * @since 1.0.0
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
@Tag(name = "Elasticsearch 商品管理", description = "商品相关操作接口")
public class ProductController {

    private final IProductService productService;

    @Operation(summary = "新增商品")
    @PostMapping("/add")
    public Result<Boolean> addProduct(@Validated(AddGroup.class) @RequestBody ProductDTO productDTO) {
        return Result.success(productService.addProduct(productDTO));
    }

    @Operation(summary = "更新商品")
    @PostMapping("/update")
    public Result<Boolean> updateProduct(@Validated(UpdateGroup.class) @RequestBody ProductDTO productDTO) {
        return Result.success(productService.updateProduct(productDTO));
    }

    @Operation(summary = "删除商品")
    @PostMapping("/delete")
    public Result<Boolean> deleteProduct(@Parameter(description = "商品ID", required = true)
                                         @NotNull(message = "商品ID不能为空") @RequestParam String id) {
        return Result.success(productService.deleteProduct(id));
    }

    @Operation(summary = "获取商品详情")
    @GetMapping("/detail")
    public Result<ProductVO> getProductDetail(@Parameter(description = "商品ID", required = true)
                                              @NotNull(message = "商品ID不能为空") @RequestParam String id) {
        return Result.success(productService.getProductDetail(id));
    }

    @Operation(summary = "分页查询商品")
    @GetMapping("/list")
    public Result<Page<ProductVO>> pageList(@ParameterObject @Validated ProductPageQuery query) {
        return Result.success(productService.pageList(query));
    }

    @Operation(summary = "批量生成测试数据")
    @GetMapping("/test/batch-insert")
    public Result<List<ProductVO>> batchInsertTestData() {
        return Result.success(productService.batchInsertTestData());
    }
}
