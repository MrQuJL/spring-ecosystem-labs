package com.spring.es.module.redis.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.spring.es.common.result.Result;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * Redis List 类型测试接口
 *
 * @author qujianlei
 * @since 1.0.0
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/api/v1/redis/list")
@RequiredArgsConstructor
@Tag(name = "Redis List测试", description = "Redis List 类型的增删改查接口")
public class RedisListController {

    private final StringRedisTemplate stringRedisTemplate;

    /**
     * 从左侧推入元素
     *
     * @param key   键
     * @param value 值
     * @return 列表长度
     */
    @Operation(summary = "从左侧推入元素")
    @PostMapping("/leftPush")
    public Result<Long> leftPush(@Parameter(description = "键", required = true, example = "list:1") @NotBlank @RequestParam String key,
                                 @Parameter(description = "值", required = true, example = "a") @NotBlank @RequestParam String value) {
        Long count = stringRedisTemplate.opsForList().leftPush(key, value);
        return Result.success(count);
    }

    /**
     * 从右侧推入元素
     *
     * @param key   键
     * @param value 值
     * @return 列表长度
     */
    @Operation(summary = "从右侧推入元素")
    @PostMapping("/rightPush")
    public Result<Long> rightPush(@Parameter(description = "键", required = true, example = "list:1") @NotBlank @RequestParam String key,
                                  @Parameter(description = "值", required = true, example = "b") @NotBlank @RequestParam String value) {
        Long count = stringRedisTemplate.opsForList().rightPush(key, value);
        return Result.success(count);
    }

    /**
     * 从左侧弹出元素
     *
     * @param key 键
     * @return 弹出的值
     */
    @Operation(summary = "从左侧弹出元素")
    @PostMapping("/leftPop")
    public Result<String> leftPop(@Parameter(description = "键", required = true, example = "list:1") @NotBlank @RequestParam String key) {
        String value = stringRedisTemplate.opsForList().leftPop(key);
        return Result.success(value);
    }

    /**
     * 从右侧弹出元素
     *
     * @param key 键
     * @return 弹出的值
     */
    @Operation(summary = "从右侧弹出元素")
    @PostMapping("/rightPop")
    public Result<String> rightPop(@Parameter(description = "键", required = true, example = "list:1") @NotBlank @RequestParam String key) {
        String value = stringRedisTemplate.opsForList().rightPop(key);
        return Result.success(value);
    }

    /**
     * 获取列表指定范围内的元素
     *
     * @param key   键
     * @param start 开始索引
     * @param end   结束索引
     * @return 元素列表
     */
    @Operation(summary = "获取列表指定范围内的元素")
    @GetMapping("/range")
    public Result<List<String>> range(@Parameter(description = "键", required = true, example = "list:1") @NotBlank @RequestParam String key,
                                      @Parameter(description = "开始索引", required = true, example = "0") @RequestParam(defaultValue = "0") long start,
                                      @Parameter(description = "结束索引", required = true, example = "-1") @RequestParam(defaultValue = "-1") long end) {
        List<String> range = stringRedisTemplate.opsForList().range(key, start, end);
        return Result.success(range);
    }

    /**
     * 获取列表长度
     *
     * @param key 键
     * @return 列表长度
     */
    @Operation(summary = "获取列表长度")
    @GetMapping("/size")
    public Result<Long> size(@Parameter(description = "键", required = true, example = "list:1") @NotBlank @RequestParam String key) {
        Long size = stringRedisTemplate.opsForList().size(key);
        return Result.success(size);
    }
}
