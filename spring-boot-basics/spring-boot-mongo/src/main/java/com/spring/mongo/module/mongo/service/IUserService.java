package com.spring.mongo.module.mongo.service;

import com.mongoplus.service.IService;
import com.spring.mongo.module.mongo.entity.User;

public interface IUserService extends IService<User> {
    /**
     * 根据名称查询用户
     * 
     * @param name 用户名
     * @return 用户实体
     */
    User findByName(String name);
}
