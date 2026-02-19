package com.spring.rocket.module.exception.controller;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import org.springdoc.api.annotations.ParameterObject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.spring.rocket.common.enums.response.ResponseEnum;
import com.spring.rocket.common.exception.BusinessException;
import com.spring.rocket.common.result.Result;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * 异常测试控制器
 * 用于测试 GlobalExceptionHandler 中的各个异常处理器
 * 
 * @author qujianlei
 * @since 1.0.0
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/api/exception-test")
@Tag(name = "异常测试", description = "用于测试 GlobalExceptionHandler 中的各个异常处理器")
public class ExceptionTestController {

    /**
     * 测试业务异常
     * 触发 BusinessException 处理器
     */
    @GetMapping("/business-exception")
    @Operation(summary = "测试业务异常")
    public Result<?> testBusinessException() {
        // 主动抛出业务异常，使用已有的 BUSINESS_ERROR 枚举值
        throw new BusinessException(ResponseEnum.BUSINESS_ERROR);
    }

    /**
     * 测试请求体参数校验异常
     * 触发 MethodArgumentNotValidException 处理器
     */
    @PostMapping("/body-validation")
    @Operation(summary = "测试请求体参数校验异常")
    public Result<?> testBodyValidation(@Valid @RequestBody TestDto testDto) {
        return Result.success("参数校验通过: " + testDto);
    }

    /**
     * 测试请求参数缺失异常
     * 触发 MissingServletRequestParameterException 处理器
     */
    @GetMapping("/missing-param")
    @Operation(summary = "测试请求参数缺失异常")
    public Result<?> testMissingParam(@RequestParam(required = true) String requiredParam) {
        return Result.success("获取到参数: " + requiredParam);
    }

    /**
     * 测试请求参数约束违反异常
     * 触发 ConstraintViolationException 处理器
     */
    @GetMapping("/param-validation")
    @Operation(summary = "测试请求参数约束违反异常")
    public Result<?> testParamValidation(
            @RequestParam @Min(value = 1, message = "ID必须大于0") Integer id,
            @RequestParam @NotBlank(message = "名称不能为空") String name) {
        return Result.success("参数校验通过: id=" + id + ", name=" + name);
    }

    /**
     * 测试模型属性绑定异常
     * 触发 BindException 处理器
     */
    @PostMapping("/bind-exception")
    @Operation(summary = "测试模型属性绑定异常")
    public Result<?> testBindException(@ParameterObject @Valid TestDto testDto) {
        return Result.success("模型绑定通过: " + testDto);
    }

    /**
     * 测试运行时异常
     * 触发 RuntimeException 处理器
     */
    @GetMapping("/runtime-exception")
    @Operation(summary = "测试运行时异常")
    public Result<?> testRuntimeException() {
        // 除0异常
        int zeroDivisor = 0;
        int result = 10 / zeroDivisor;
        return Result.success("结果: " + result);
    }

    /**
     * 测试系统异常
     * 触发 Exception 处理器（兜底）
     */
    @GetMapping("/system-exception")
    @Operation(summary = "测试系统异常")
    public Result<?> testSystemException() throws Exception {
        // 主动抛出 Exception
        throw new Exception("测试系统异常");
    }

    @Data
    public static class TestDto {
        @NotBlank(message = "名称不能为空")
        private String name;

        @Min(value = 18, message = "年龄必须大于等于18")
        private Integer age;
    }
}