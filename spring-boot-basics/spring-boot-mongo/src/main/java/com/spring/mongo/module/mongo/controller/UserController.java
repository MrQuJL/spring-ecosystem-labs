package com.spring.mongo.module.mongo.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.mongo.common.result.Result;
import com.spring.mongo.module.mongo.entity.User;
import com.spring.mongo.module.mongo.service.IUserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 用户API接口
 *
 * @author qujianlei
 * @since 1.0.0
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Tag(name = "用户管理", description = "用户的增删改查接口")
public class UserController {
    
    private final IUserService userService;

    @Operation(summary = "添加用户")
    @GetMapping("/add")
    public Result<User> add() {
        User user = new User();
        user.setName("hello");
        user.setAge(Long.valueOf(18));
        user.setEmail("dsfjksf@134.com");
        userService.save(user);
        return Result.success();
    }
}
