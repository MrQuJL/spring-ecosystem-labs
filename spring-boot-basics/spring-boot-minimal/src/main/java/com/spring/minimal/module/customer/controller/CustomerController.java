package com.spring.minimal.module.customer.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.spring.minimal.common.result.Result;
import com.spring.minimal.common.validation.AddGroup;
import com.spring.minimal.common.validation.UpdateGroup;
import com.spring.minimal.module.customer.dto.CustomerDTO;
import com.spring.minimal.module.customer.dto.CustomerPageQuery;
import com.spring.minimal.module.customer.dto.CustomerStatusReq;
import com.spring.minimal.module.customer.service.ICustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springdoc.api.annotations.ParameterObject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.spring.minimal.module.customer.vo.CustomerVO;

/**
 * 客户API接口
 *
 * @author qujianlei
 * @since 1.0.0
 */
@Slf4j
@Validated
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
    @PostMapping("/add")
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
    @PostMapping("/update")
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
    @GetMapping("/detail")
    public Result<Object> getCustomer(@Parameter(description = "客户ID", required = true)
                                      @NotNull(message = "客户ID不能为空") @RequestParam Long id) {
        return Result.success(customerService.getById(id));
    }

    /**
     * 分页查询客户
     *
     * @param query 查询条件
     * @return 分页结果
     */
    @Operation(summary = "分页查询客户")
    @GetMapping("/list")
    public Result<Page<CustomerVO>> list(@ParameterObject @Valid CustomerPageQuery query) {
        return Result.success(customerService.pageList(query));
    }

    /**
     * 更新客户状态
     *
     * @param req 状态更新请求
     * @return 是否成功
     */
    @Operation(summary = "更新客户状态")
    @PostMapping("/updateStatus")
    public Result<Boolean> updateStatus(@Valid @RequestBody CustomerStatusReq req) {
        return Result.success(customerService.updateStatus(req));
    }
}
