package com.spring.rocket.common.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * MyBatis Plus 自动填充配置
 * <p>
 * 1. 插入时自动填充 createdAt 和 updatedAt 字段
 * 2. 更新时自动填充 updatedAt 字段
 * 3. 确保实体类的 createdAt 和 updatedAt 字段为 LocalDateTime 类型
 * <p>
 *
 * @author qujianlei
 * @since 1.0.0
 */
@Slf4j
@Component
public class MybatisPlusMetaObjectHandler implements MetaObjectHandler {

    /**
     * 插入时自动填充 createdAt 和 updatedAt 字段
     *
     * @param metaObject MyBatis Plus 元对象
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("start insert fill ....");
        this.strictInsertFill(metaObject, "createdAt", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "updatedAt", LocalDateTime.class, LocalDateTime.now());
    }

    /**
     * 更新时自动填充 updatedAt 字段
     *
     * @param metaObject MyBatis Plus 元对象
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("start update fill ....");
        // 强制更新 updatedAt，无论原值是否为空
        this.setFieldValByName("updatedAt", LocalDateTime.now(), metaObject);
    }
}
