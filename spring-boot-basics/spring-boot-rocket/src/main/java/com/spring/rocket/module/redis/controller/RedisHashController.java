package com.spring.rocket.module.redis.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.spring.rocket.common.result.Result;

import javax.validation.constraints.NotBlank;
import java.util.Map;

/**
 * Redis Hash 类型测试接口
 *
 * @author qujianlei
 * @since 1.0.0
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/api/v1/redis/hash")
@RequiredArgsConstructor
@Tag(name = "Redis Hash测试", description = "Redis Hash 类型的增删改查接口")
public class RedisHashController {

    private final StringRedisTemplate stringRedisTemplate;

    /**
     * 设置Hash键值对
     *
     * @param key     键
     * @param hashKey Hash键
     * @param value   值
     * @return 是否成功
     */
    @Operation(summary = "设置Hash键值对")
    @PostMapping("/put")
    public Result<Boolean> put(@Parameter(description = "键", required = true, example = "user:1") @NotBlank @RequestParam String key,
                               @Parameter(description = "Hash键", required = true, example = "name") @NotBlank @RequestParam String hashKey,
                               @Parameter(description = "值", required = true, example = "张三") @NotBlank @RequestParam String value) {
        stringRedisTemplate.opsForHash().put(key, hashKey, value);
        return Result.success(true);
    }

    /**
     * 获取Hash值
     *
     * @param key     键
     * @param hashKey Hash键
     * @return 值
     */
    @Operation(summary = "获取Hash值")
    @GetMapping("/get")
    public Result<Object> get(@Parameter(description = "键", required = true, example = "user:1") @NotBlank @RequestParam String key,
                              @Parameter(description = "Hash键", required = true, example = "name") @NotBlank @RequestParam String hashKey) {
        Object value = stringRedisTemplate.opsForHash().get(key, hashKey);
        return Result.success(value);
    }

    /**
     * 删除Hash键
     *
     * @param key     键
     * @param hashKey Hash键
     * @return 成功删除的数量
     */
    @Operation(summary = "删除Hash键")
    @PostMapping("/delete")
    public Result<Long> delete(@Parameter(description = "键", required = true, example = "user:1") @NotBlank @RequestParam String key,
                               @Parameter(description = "Hash键", required = true, example = "name") @NotBlank @RequestParam String hashKey) {
        Long result = stringRedisTemplate.opsForHash().delete(key, hashKey);
        return Result.success(result);
    }

    /**
     * 获取Hash所有键值对
     *
     * @param key 键
     * @return 所有键值对
     */
    @Operation(summary = "获取Hash所有键值对")
    @GetMapping("/entries")
    public Result<Map<Object, Object>> entries(@Parameter(description = "键", required = true, example = "user:1") @NotBlank @RequestParam String key) {
        Map<Object, Object> entries = stringRedisTemplate.opsForHash().entries(key);
        return Result.success(entries);
    }

    /**
     * 判断Hash键是否存在
     *
     * @param key     键
     * @param hashKey Hash键
     * @return 是否存在
     */
    @Operation(summary = "判断Hash键是否存在")
    @GetMapping("/hasKey")
    public Result<Boolean> hasKey(@Parameter(description = "键", required = true, example = "user:1") @NotBlank @RequestParam String key,
                                  @Parameter(description = "Hash键", required = true, example = "name") @NotBlank @RequestParam String hashKey) {
        Boolean result = stringRedisTemplate.opsForHash().hasKey(key, hashKey);
        return Result.success(result);
    }
}
