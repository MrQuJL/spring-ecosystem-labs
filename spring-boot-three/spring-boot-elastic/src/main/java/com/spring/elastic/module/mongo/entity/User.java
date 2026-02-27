package com.spring.elastic.module.mongo.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.mongoplus.annotation.ID;
import com.mongoplus.annotation.collection.CollectionField;
import com.mongoplus.annotation.collection.CollectionLogic;
import com.mongoplus.annotation.collection.CollectionName;
import com.mongoplus.enums.FieldFill;

/**
 * 用户实体类
 *
 * @author qujianlei
 * @since 1.0.0
 */
@Data
@CollectionName("user")
public class User {

    /**
     * 用户ID
     */
    @ID
    private String id;

    /**
     * 用户名称
     */
    private String name;

    /**
     * 用户年龄
     */
    private Long age;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 账户余额
     */
    private BigDecimal balance;

    /**
     * 用户状态: 0-冻结, 1-正常
     */
    private Integer status;

    /**
     * 创建时间
     */
    @CollectionField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @CollectionField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    /**
     * 逻辑删除: 0-未删除, 1-已删除
     */
    @CollectionLogic
    private Integer deleted;
}
