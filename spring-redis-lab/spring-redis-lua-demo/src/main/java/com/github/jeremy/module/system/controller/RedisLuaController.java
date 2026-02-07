package com.github.jeremy.module.system.controller;

import com.github.jeremy.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.Collections;

/**
 * Redis Lua 脚本原子性测试示例
 * 演示通过 Lua 脚本实现 "查询-判断-修改" 的原子性操作
 */
@RestController
@RequestMapping("/api/redis-lua")
@Api(tags = "Redis Lua 原子性测试")
@Slf4j
public class RedisLuaController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private DefaultRedisScript<Long> deductStockScript;

    @PostConstruct
    public void init() {
        deductStockScript = new DefaultRedisScript<>();
        deductStockScript.setResultType(Long.class);
        deductStockScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("lua/deduct_stock.lua")));
    }

    @ApiOperation("初始化库存")
    @PostMapping("/init-stock")
    public Result<Boolean> initStock(@ApiParam(value = "库存Key", required = true, example = "product:101:stock")
                                   @RequestParam String key,
                                   @ApiParam(value = "库存数量", required = true, example = "100")
                                   @RequestParam Integer count) {
        stringRedisTemplate.opsForValue().set(key, String.valueOf(count));
        log.info("初始化库存: key={}, count={}", key, count);
        return Result.success(true);
    }

    @ApiOperation("Lua 脚本扣减库存（原子性操作）")
    @PostMapping("/deduct-stock")
    public Result<String> deductStock(@ApiParam(value = "库存Key", required = true, example = "product:101:stock")
                                     @RequestParam String key,
                                     @ApiParam(value = "扣减数量", required = true, example = "1")
                                     @RequestParam Integer amount) {
        // 1. 执行脚本
        // execute(RedisScript<T> script, List<K> keys, Object... args)
        Long result = stringRedisTemplate.execute(deductStockScript, Collections.singletonList(key), String.valueOf(amount));

        if (Long.valueOf(1).equals(result)) {
            log.info("库存扣减成功: key={}, amount={}", key, amount);
            return Result.success("扣减成功");
        } else {
            log.warn("库存不足，扣减失败: key={}, amount={}", key, amount);
            return Result.fail("库存不足，扣减失败");
        }
    }

    @ApiOperation("Java 端不安全扣减（演示并发问题）")
    @PostMapping("/deduct-stock-unsafe")
    public Result<String> deductStockUnsafe(@ApiParam(value = "库存Key", required = true, example = "product:101:stock")
                                           @RequestParam String key,
                                           @ApiParam(value = "扣减数量", required = true, example = "1")
                                           @RequestParam Integer amount) {
        // 1. 从 Redis 获取当前库存（Java 读）
        String currentStockStr = stringRedisTemplate.opsForValue().get(key);
        int currentStock = currentStockStr == null ? 0 : Integer.parseInt(currentStockStr);

        // 2. 在 Java 代码中判断（Java 判断）
        if (currentStock >= amount) {
            // 模拟业务耗时，让并发问题更容易出现
            try { Thread.sleep(100); } catch (InterruptedException e) {}

            // 3. 在 Java 中计算新库存（Java 计算）
            int newStock = currentStock - amount;

            // 4. 写回 Redis（Java 写，不加任何判断，直接覆盖）
            stringRedisTemplate.opsForValue().set(key, String.valueOf(newStock));

            log.info("不安全扣减成功: key={}, newStock={}", key, newStock);
            return Result.success("不安全扣减成功，当前余量：" + newStock);
        } else {
            return Result.fail("库存不足");
        }
    }
}
