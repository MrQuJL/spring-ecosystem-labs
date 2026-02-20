package com.spring.mongo.module.mongo.entity;

import lombok.Data;

import com.mongoplus.annotation.ID;
import com.mongoplus.annotation.collection.CollectionName;

@Data
@CollectionName("user")
public class User {
    @ID
    private String id;
    private String name;
    private Long age;
    private String email;
}
