package com.spring.elastic.module.elastic.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "products") // 对应 ES 索引名
public class Product {

    /**
     * ES 文档 ID (_id)
     * 对应之前的 discussion: URL 中的 1
     */
    @Id
    private String id;

    /**
     * 业务商品 ID
     * 类型: long
     * 对应之前的 discussion: 请求体中的 product_id
     */
    @Field(type = FieldType.Long)
    private Long productId;

    /**
     * 商品名称
     * 类型: text (支持全文搜索) + keyword (支持聚合/排序)
     * Multi-fields 示例
     */
    @Field(type = FieldType.Text, analyzer = "standard")
    private String productName;

    /**
     * 分类
     * 类型: keyword (精确匹配/聚合)
     */
    @Field(type = FieldType.Keyword)
    private String category;

    /**
     * 品牌
     * 类型: keyword
     */
    @Field(type = FieldType.Keyword)
    private String brand;

    /**
     * 价格
     * 类型: double
     */
    @Field(type = FieldType.Double)
    private Double price;

    /**
     * 库存
     * 类型: integer
     */
    @Field(type = FieldType.Integer)
    private Integer stock;

    /**
     * 是否上架
     * 类型: boolean
     */
    @Field(type = FieldType.Boolean)
    private Boolean isOnSale;

    /**
     * 创建时间
     * 类型: date
     * 格式: uuuu-MM-dd HH:mm:ss
     */
    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second)
    private LocalDateTime createdAt;
}