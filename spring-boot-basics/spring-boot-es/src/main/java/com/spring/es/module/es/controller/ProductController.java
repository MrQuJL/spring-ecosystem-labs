package com.spring.es.module.es.controller;

import com.spring.es.common.result.Result;
import com.spring.es.module.es.entity.Product;
import com.spring.es.module.es.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/v1/es/product")
@RequiredArgsConstructor
@Tag(name = "Elasticsearch Product测试", description = "Elasticsearch 产品增删改查接口")
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "新增产品")
    @PostMapping("/save")
    public Result<Product> save(@RequestBody Product product) {
        Product savedProduct = productService.save(product);
        return Result.success(savedProduct);
    }

    @Operation(summary = "批量新增产品")
    @PostMapping("/saveAll")
    public Result<List<Product>> saveAll(@RequestBody List<Product> products) {
        List<Product> savedProducts = productService.saveAll(products);
        return Result.success(savedProducts);
    }

    @Operation(summary = "根据ID查询产品")
    @GetMapping("/findById/{id}")
    public Result<Product> findById(@Parameter(description = "产品ID", required = true) @PathVariable String id) {
        return productService.findById(id)
                .map(Result::success)
                .orElse(Result.fail("产品不存在"));
    }

    @Operation(summary = "查询所有产品")
    @GetMapping("/findAll")
    public Result<List<Product>> findAll() {
        List<Product> products = productService.findAll();
        return Result.success(products);
    }

    @Operation(summary = "根据名称模糊查询产品")
    @GetMapping("/findByName")
    public Result<List<Product>> findByName(@Parameter(description = "产品名称", required = true) @RequestParam String name) {
        List<Product> products = productService.findByNameContaining(name);
        return Result.success(products);
    }

    @Operation(summary = "根据分类查询产品")
    @GetMapping("/findByCategory")
    public Result<List<Product>> findByCategory(@Parameter(description = "产品分类", required = true) @RequestParam String category) {
        List<Product> products = productService.findByCategory(category);
        return Result.success(products);
    }

    @Operation(summary = "根据品牌查询产品")
    @GetMapping("/findByBrand")
    public Result<List<Product>> findByBrand(@Parameter(description = "产品品牌", required = true) @RequestParam String brand) {
        List<Product> products = productService.findByBrand(brand);
        return Result.success(products);
    }

    @Operation(summary = "更新产品")
    @PutMapping("/update")
    public Result<Product> update(@RequestBody Product product) {
        Product updatedProduct = productService.update(product);
        return Result.success(updatedProduct);
    }

    @Operation(summary = "根据ID删除产品")
    @DeleteMapping("/deleteById/{id}")
    public Result<Void> deleteById(@Parameter(description = "产品ID", required = true) @PathVariable String id) {
        productService.deleteById(id);
        return Result.success(null);
    }

    @Operation(summary = "删除所有产品")
    @DeleteMapping("/deleteAll")
    public Result<Void> deleteAll() {
        productService.deleteAll();
        return Result.success(null);
    }
}
