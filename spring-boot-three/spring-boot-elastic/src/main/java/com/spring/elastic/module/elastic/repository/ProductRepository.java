package com.spring.elastic.module.elastic.repository;


import com.spring.elastic.module.elastic.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends ElasticsearchRepository<Product, String> {

    // --- 基础 CRUD 由父接口提供: save, findById, delete, findAll ---

    /**
     * 自定义查询：根据业务商品 ID 精确查询 (Term Query)
     * 方法名规则: findBy + 字段名 (首字母大写)
     */
    Optional<Product> findByProductId(Long productId);

    /**
     * 自定义查询：根据商品名称模糊搜索 (Match Query)
     * 方法名规则: findBy + 字段名 (首字母大写)
     */
    List<Product> findByProductNameContaining(String productName);

    /**
     * 自定义查询：根据分类和是否上架筛选 (Bool Query + Filter)
     */
    List<Product> findByCategoryAndIsOnSale(String category, Boolean isOnSale);

    /**
     * 自定义查询：价格范围查询
     */
    List<Product> findByPriceBetween(Double minPrice, Double maxPrice);

    /**
     * 分页查询：根据品牌
     */
    Page<Product> findByBrand(String brand, Pageable pageable);
    
    /**
     * 删除操作：根据业务 ID 删除
     */
    void deleteByProductId(Long productId);
}