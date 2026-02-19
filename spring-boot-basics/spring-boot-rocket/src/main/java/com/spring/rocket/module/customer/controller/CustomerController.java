package com.spring.rocket.module.customer.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.spring.rocket.common.result.Result;
import com.spring.rocket.common.validation.AddGroup;
import com.spring.rocket.common.validation.UpdateGroup;
import com.spring.rocket.module.customer.dto.CustomerDTO;
import com.spring.rocket.module.customer.dto.CustomerPageQuery;
import com.spring.rocket.module.customer.dto.CustomerStatusReq;
import com.spring.rocket.module.customer.service.ICustomerService;
import com.spring.rocket.module.customer.vo.CustomerVO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springdoc.api.annotations.ParameterObject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

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

    /**
     * 删除客户
     *
     * @param id 客户ID
     * @return 是否成功
     */
    @Operation(summary = "删除客户")
    @PostMapping("/delete")
    public Result<Boolean> deleteCustomer(@Parameter(description = "客户ID", required = true, example = "1")
                                          @NotNull(message = "客户ID不能为空") @RequestParam Long id) {
        return Result.success(customerService.deleteCustomer(id));
    }

    /**
     * 获取客户详情
     *
     * @param id 客户ID
     * @return 客户信息
     */
    @Operation(summary = "获取客户详情")
    @GetMapping("/detail")
    public Result<CustomerVO> getCustomer(@Parameter(description = "客户ID", required = true, example = "1")
                                          @NotNull(message = "客户ID不能为空") @RequestParam Long id) {
        return Result.success(customerService.getCustomer(id));
    }

    /**
     * 分页查询客户
     *
     * @param query 查询条件
     * @return 分页结果
     */
    @Operation(summary = "分页查询客户")
    @GetMapping("/list")
    public Result<IPage<CustomerVO>> list(@ParameterObject @Valid CustomerPageQuery query) {
        return Result.success(customerService.pageList(query));
    }

    /**
     * 导出客户数据
     *
     * @param response 响应对象
     * @param status   客户状态
     */
    @Operation(summary = "导出客户数据")
    @GetMapping("/export")
    public void export(HttpServletResponse response,
                       @Parameter(description = "客户状态", required = false, example = "1")
                       @RequestParam(required = false) Integer status) {
        customerService.exportCustomer(response, status);
    }

    /**
     * 导入客户数据
     *
     * @param file          Excel文件
     * @param operatorName  操作人名称
     * @param operatorId    操作人ID
     * @return 是否成功
     */
    @Operation(summary = "导入客户数据")
    @PostMapping(value = "/import", consumes = "multipart/form-data")
    public Result<Boolean> importCustomer(@Parameter(description = "Excel文件", required = true) @RequestPart("file") MultipartFile file,
                                          @Parameter(description = "操作人名称", required = true, example = "张三") @RequestParam String operatorName,
                                          @Parameter(description = "操作人ID", required = true, example = "1") @RequestParam Long operatorId) {
        customerService.importCustomer(file, operatorName, operatorId);
        return Result.success(true);
    }
}
