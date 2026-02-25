package com.spring.elastic.module.redis.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.spring.elastic.common.result.Result;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.concurrent.TimeUnit;

/**
 * Redis String 类型测试接口
 *
 * @author qujianlei
 * @since 1.0.0
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/api/v1/redis/string")
@RequiredArgsConstructor
@Tag(name = "Redis String测试", description = "Redis String 类型的增删改查接口")
public class RedisStringController {

    private final StringRedisTemplate stringRedisTemplate;

    /**
     * 设置键值对
     *
     * @param key   键
     * @param value 值
     * @return 是否成功
     */
    @Operation(summary = "设置键值对")
    @PostMapping("/set")
    public Result<Boolean> set(@Parameter(description = "键", required = true, example = "name") @NotBlank @RequestParam String key,
                               @Parameter(description = "值", required = true, example = "张三") @NotBlank @RequestParam String value) {
        stringRedisTemplate.opsForValue().set(key, value);
        return Result.success(true);
    }

    /**
     * 设置键值对（带过期时间）
     *
     * @param key     键
     * @param value   值
     * @param timeout 过期时间（秒）
     * @return 是否成功
     */
    @Operation(summary = "设置键值对（带过期时间）")
    @PostMapping("/setEx")
    public Result<Boolean> setEx(@Parameter(description = "键", required = true, example = "code") @NotBlank @RequestParam String key,
                                 @Parameter(description = "值", required = true, example = "123456") @NotBlank @RequestParam String value,
                                 @Parameter(description = "过期时间(秒)", required = true, example = "60") @NotNull @RequestParam Long timeout) {
        stringRedisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
        return Result.success(true);
    }

    /**
     * 获取值
     *
     * @param key 键
     * @return 值
     */
    @Operation(summary = "获取值")
    @GetMapping("/get")
    public Result<String> get(@Parameter(description = "键", required = true, example = "name") @NotBlank @RequestParam String key) {
        String value = stringRedisTemplate.opsForValue().get(key);
        return Result.success(value);
    }

    /**
     * 递增
     *
     * @param key   键
     * @param delta 增量
     * @return 递增后的值
     */
    @Operation(summary = "递增")
    @PostMapping("/increment")
    public Result<Long> increment(@Parameter(description = "键", required = true, example = "count") @NotBlank @RequestParam String key,
                                  @Parameter(description = "增量", required = true, example = "1") @NotNull @RequestParam Long delta) {
        Long result = stringRedisTemplate.opsForValue().increment(key, delta);
        return Result.success(result);
    }

    /**
     * 递减
     *
     * @param key   键
     * @param delta 减量
     * @return 递减后的值
     */
    @Operation(summary = "递减")
    @PostMapping("/decrement")
    public Result<Long> decrement(@Parameter(description = "键", required = true, example = "count") @NotBlank @RequestParam String key,
                                  @Parameter(description = "减量", required = true, example = "1") @NotNull @RequestParam Long delta) {
        Long result = stringRedisTemplate.opsForValue().decrement(key, delta);
        return Result.success(result);
    }

    /**
     * 删除键
     *
     * @param key 键
     * @return 是否成功
     */
    @Operation(summary = "删除键")
    @PostMapping("/delete")
    public Result<Boolean> delete(@Parameter(description = "键", required = true, example = "name") @NotBlank @RequestParam String key) {
        Boolean result = stringRedisTemplate.delete(key);
        return Result.success(result);
    }
}
