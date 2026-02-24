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

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.Set;

/**
 * Redis Set 类型测试接口
 *
 * @author qujianlei
 * @since 1.0.0
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/api/v1/redis/set")
@RequiredArgsConstructor
@Tag(name = "Redis Set测试", description = "Redis Set 类型的增删改查接口")
public class RedisSetController {

    private final StringRedisTemplate stringRedisTemplate;

    /**
     * 向集合中添加元素
     *
     * @param key    键
     * @param values 值
     * @return 添加成功的数量
     */
    @Operation(summary = "向集合中添加元素")
    @PostMapping("/add")
    public Result<Long> add(@Parameter(description = "键", required = true, example = "set:1") @NotBlank @RequestParam String key,
                            @Parameter(description = "值", required = true, example = "a,b,c") @NotEmpty @RequestParam String[] values) {
        Long count = stringRedisTemplate.opsForSet().add(key, values);
        return Result.success(count);
    }

    /**
     * 从集合中移除元素
     *
     * @param key    键
     * @param values 值
     * @return 移除成功的数量
     */
    @Operation(summary = "从集合中移除元素")
    @PostMapping("/remove")
    public Result<Long> remove(@Parameter(description = "键", required = true, example = "set:1") @NotBlank @RequestParam String key,
                               @Parameter(description = "值", required = true, example = "a,b") @NotEmpty @RequestParam String[] values) {
        Long count = stringRedisTemplate.opsForSet().remove(key, (Object[]) values);
        return Result.success(count);
    }

    /**
     * 获取集合中的所有元素
     *
     * @param key 键
     * @return 所有元素
     */
    @Operation(summary = "获取集合中的所有元素")
    @GetMapping("/members")
    public Result<Set<String>> members(@Parameter(description = "键", required = true, example = "set:1") @NotBlank @RequestParam String key) {
        Set<String> members = stringRedisTemplate.opsForSet().members(key);
        return Result.success(members);
    }

    /**
     * 判断元素是否在集合中
     *
     * @param key   键
     * @param value 值
     * @return 是否存在
     */
    @Operation(summary = "判断元素是否在集合中")
    @GetMapping("/isMember")
    public Result<Boolean> isMember(@Parameter(description = "键", required = true, example = "set:1") @NotBlank @RequestParam String key,
                                    @Parameter(description = "值", required = true, example = "a") @NotBlank @RequestParam String value) {
        Boolean isMember = stringRedisTemplate.opsForSet().isMember(key, value);
        return Result.success(isMember);
    }

    /**
     * 获取集合的大小
     *
     * @param key 键
     * @return 集合大小
     */
    @Operation(summary = "获取集合的大小")
    @GetMapping("/size")
    public Result<Long> size(@Parameter(description = "键", required = true, example = "set:1") @NotBlank @RequestParam String key) {
        Long size = stringRedisTemplate.opsForSet().size(key);
        return Result.success(size);
    }
}
