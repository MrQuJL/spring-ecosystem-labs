package com.spring.elastic.module.mongo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import com.mongoplus.model.PageResult;
import com.spring.elastic.common.result.Result;
import com.spring.elastic.common.validation.AddGroup;
import com.spring.elastic.common.validation.UpdateGroup;
import com.spring.elastic.module.mongo.dto.UserDTO;
import com.spring.elastic.module.mongo.dto.UserPageQuery;
import com.spring.elastic.module.mongo.dto.UserStatusReq;
import com.spring.elastic.module.mongo.service.IUserService;
import com.spring.elastic.module.mongo.vo.UserVO;

/**
 * 客户API接口
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

    /**
     * 新增用户
     *
     * @param userDTO 用户信息
     * @return 是否成功
     */
    @Operation(summary = "新增用户")
    @PostMapping("/add")
    public Result<Boolean> addUser(@Validated(AddGroup.class) @RequestBody UserDTO userDTO) {
        return Result.success(userService.addUser(userDTO));
    }

    /**
     * 更新用户
     *
     * @param userDTO 用户信息
     * @return 是否成功
     */
    @Operation(summary = "更新用户")
    @PostMapping("/update")
    public Result<Boolean> updateUser(@Validated(UpdateGroup.class) @RequestBody UserDTO userDTO) {
        return Result.success(userService.updateUser(userDTO));
    }

    /**
     * 更新用户状态
     *
     * @param req 状态更新请求
     * @return 是否成功
     */
    @Operation(summary = "更新用户状态")
    @PostMapping("/updateStatus")
    public Result<Boolean> updateStatus(@Valid @RequestBody UserStatusReq req) {
        return Result.success(userService.updateStatus(req));
    }

    /**
     * 删除用户
     *
     * @param id 用户ID
     * @return 是否成功
     */
    @Operation(summary = "删除用户")
    @PostMapping("/delete")
    public Result<Boolean> deleteUser(@Parameter(description = "用户ID", required = true, example = "6997e7948b6117674b5d3aa9")
                                      @NotNull(message = "用户ID不能为空") @RequestParam String id) {
        return Result.success(userService.deleteUser(id));
    }

    /**
     * 获取用户详情
     *
     * @param id 用户ID
     * @return 用户信息
     */
    @Operation(summary = "获取用户详情")
    @GetMapping("/detail")
    public Result<UserVO> getUser(@Parameter(description = "用户ID", required = true, example = "6997e7948b6117674b5d3aa9")
                                  @NotNull(message = "用户ID不能为空") @RequestParam String id) {
        return Result.success(userService.getUser(id));
    }

    /**
     * 分页查询用户
     *
     * @param query 查询条件
     * @return 分页结果
     */
    @Operation(summary = "分页查询用户")
    @GetMapping("/list")
    public Result<PageResult<UserVO>> list(@ParameterObject @Valid UserPageQuery query) {
        return Result.success(userService.pageList(query));
    }
}
