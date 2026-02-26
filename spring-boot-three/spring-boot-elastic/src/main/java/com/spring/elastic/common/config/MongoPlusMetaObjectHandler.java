package com.spring.elastic.common.config;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.mongoplus.handlers.MetaObjectHandler;
import com.mongoplus.model.AutoFillMetaObject;

/**
 * MongoPlus 自动填充处理器
 *
 * @author qujianlei
 * @since 1.0.0
 */
@Component
public class MongoPlusMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(AutoFillMetaObject metaObject) {
        LocalDateTime now = LocalDateTime.now();
        metaObject.fillValue("createdAt", now);
        metaObject.fillValue("updatedAt", now);
    }

    @Override
    public void updateFill(AutoFillMetaObject metaObject) {
        metaObject.fillValue("updatedAt", LocalDateTime.now());
    }
}
