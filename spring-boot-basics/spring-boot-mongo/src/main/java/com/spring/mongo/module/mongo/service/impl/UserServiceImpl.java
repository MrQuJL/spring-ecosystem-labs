package com.spring.mongo.module.mongo.service.impl;

import org.springframework.stereotype.Service;

import com.mongoplus.service.impl.ServiceImpl;
import com.spring.mongo.module.mongo.entity.User;
import com.spring.mongo.module.mongo.service.IUserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<User> implements IUserService {

    @Override
    public User findByName(String name) {
        User user = this.lambdaQuery()
            .eq(User::getName, name)
            .one();
        return user;
    }
}
