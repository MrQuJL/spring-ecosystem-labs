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
import java.util.Set;

/**
 * Redis ZSet (Sorted Set) 类型测试接口
 *
 * @author qujianlei
 * @since 1.0.0
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/api/v1/redis/zset")
@RequiredArgsConstructor
@Tag(name = "Redis ZSet测试", description = "Redis ZSet 类型的增删改查接口")
public class RedisZSetController {

    private final StringRedisTemplate stringRedisTemplate;

    /**
     * 添加元素
     *
     * @param key   键
     * @param value 值
     * @param score 分数
     * @return 是否成功
     */
    @Operation(summary = "添加元素")
    @PostMapping("/add")
    public Result<Boolean> add(@Parameter(description = "键", required = true, example = "rank:1") @NotBlank @RequestParam String key,
                               @Parameter(description = "值", required = true, example = "player1") @NotBlank @RequestParam String value,
                               @Parameter(description = "分数", required = true, example = "100.0") @NotNull @RequestParam Double score) {
        Boolean result = stringRedisTemplate.opsForZSet().add(key, value, score);
        return Result.success(result);
    }

    /**
     * 移除元素
     *
     * @param key   键
     * @param value 值
     * @return 移除成功的数量
     */
    @Operation(summary = "移除元素")
    @PostMapping("/remove")
    public Result<Long> remove(@Parameter(description = "键", required = true, example = "rank:1") @NotBlank @RequestParam String key,
                               @Parameter(description = "值", required = true, example = "player1") @NotBlank @RequestParam String value) {
        Long count = stringRedisTemplate.opsForZSet().remove(key, value);
        return Result.success(count);
    }

    /**
     * 获取元素的分数
     *
     * @param key   键
     * @param value 值
     * @return 分数
     */
    @Operation(summary = "获取元素的分数")
    @GetMapping("/score")
    public Result<Double> score(@Parameter(description = "键", required = true, example = "rank:1") @NotBlank @RequestParam String key,
                                @Parameter(description = "值", required = true, example = "player1") @NotBlank @RequestParam String value) {
        Double score = stringRedisTemplate.opsForZSet().score(key, value);
        return Result.success(score);
    }

    /**
     * 获取元素的排名（从低到高）
     *
     * @param key   键
     * @param value 值
     * @return 排名
     */
    @Operation(summary = "获取元素的排名（从低到高）")
    @GetMapping("/rank")
    public Result<Long> rank(@Parameter(description = "键", required = true, example = "rank:1") @NotBlank @RequestParam String key,
                             @Parameter(description = "值", required = true, example = "player1") @NotBlank @RequestParam String value) {
        Long rank = stringRedisTemplate.opsForZSet().rank(key, value);
        return Result.success(rank);
    }

    /**
     * 获取元素的排名（从高到低）
     *
     * @param key   键
     * @param value 值
     * @return 排名
     */
    @Operation(summary = "获取元素的排名（从高到低）")
    @GetMapping("/reverseRank")
    public Result<Long> reverseRank(@Parameter(description = "键", required = true, example = "rank:1") @NotBlank @RequestParam String key,
                                    @Parameter(description = "值", required = true, example = "player1") @NotBlank @RequestParam String value) {
        Long rank = stringRedisTemplate.opsForZSet().reverseRank(key, value);
        return Result.success(rank);
    }

    /**
     * 获取指定范围内的元素（按分数从低到高）
     *
     * @param key   键
     * @param start 开始索引
     * @param end   结束索引
     * @return 元素集合
     */
    @Operation(summary = "获取指定范围内的元素")
    @GetMapping("/range")
    public Result<Set<String>> range(@Parameter(description = "键", required = true, example = "rank:1") @NotBlank @RequestParam String key,
                                     @Parameter(description = "开始索引", required = true, example = "0") @RequestParam(defaultValue = "0") long start,
                                     @Parameter(description = "结束索引", required = true, example = "-1") @RequestParam(defaultValue = "-1") long end) {
        Set<String> range = stringRedisTemplate.opsForZSet().range(key, start, end);
        return Result.success(range);
    }

    /**
     * 获取指定分数范围内的元素
     *
     * @param key 键
     * @param min 最小分数
     * @param max 最大分数
     * @return 元素集合
     */
    @Operation(summary = "获取指定分数范围内的元素")
    @GetMapping("/rangeByScore")
    public Result<Set<String>> rangeByScore(@Parameter(description = "键", required = true, example = "rank:1") @NotBlank @RequestParam String key,
                                            @Parameter(description = "最小分数", required = true, example = "0") @RequestParam double min,
                                            @Parameter(description = "最大分数", required = true, example = "100") @RequestParam double max) {
        Set<String> range = stringRedisTemplate.opsForZSet().rangeByScore(key, min, max);
        return Result.success(range);
    }

    /**
     * 获取集合的大小
     *
     * @param key 键
     * @return 集合大小
     */
    @Operation(summary = "获取集合的大小")
    @GetMapping("/size")
    public Result<Long> size(@Parameter(description = "键", required = true, example = "rank:1") @NotBlank @RequestParam String key) {
        Long size = stringRedisTemplate.opsForZSet().zCard(key);
        return Result.success(size);
    }
}
