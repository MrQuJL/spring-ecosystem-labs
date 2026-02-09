package com.spring.minimal.module.customer.controller;

import com.spring.minimal.common.result.Result;
import com.spring.minimal.common.validation.AddGroup;
import com.spring.minimal.common.validation.UpdateGroup;
import com.spring.minimal.module.customer.dto.CustomerDTO;
import com.spring.minimal.module.customer.service.ICustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 客户控制器
 *
 * @author qujianlei
 * @since 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/customer")
@RequiredArgsConstructor
@Tag(name = "客户管理", description = "客户的增删改查接口")
public class CustomerController {

    private final ICustomerService customerService;

    /**
     * 新增客户
     *
     * @param customerDTO 客户信息
     * @return 是否成功
     */
    @Operation(summary = "新增客户")
    @PostMapping
    public Result<Boolean> addCustomer(@Validated(AddGroup.class) @RequestBody CustomerDTO customerDTO) {
        return Result.success(customerService.addCustomer(customerDTO));
    }

    /**
     * 更新客户
     *
     * @param customerDTO 客户信息
     * @return 是否成功
     */
    @Operation(summary = "更新客户")
    @PutMapping
    public Result<Boolean> updateCustomer(@Validated(UpdateGroup.class) @RequestBody CustomerDTO customerDTO) {
        return Result.success(customerService.updateCustomer(customerDTO));
    }

    /**
     * 获取客户详情
     *
     * @param id 客户ID
     * @return 客户信息
     */
    @Operation(summary = "获取客户详情")
    @GetMapping("/{id}")
    public Result<Object> getCustomer(@PathVariable Long id) {
        return Result.success(customerService.getById(id));
    }
}
