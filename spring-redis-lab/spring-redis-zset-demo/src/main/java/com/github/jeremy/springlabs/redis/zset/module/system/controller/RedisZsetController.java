package com.github.jeremy.springlabs.redis.zset.module.system.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.jeremy.springlabs.redis.zset.common.result.Result;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("/api/redis-zset")
@Api(tags = "Redis ZSet测试接口")
@Validated
@Slf4j
public class RedisZsetController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @ApiOperation("批量造数（排行榜示例）")
    @PostMapping("/seed")
    public Result<Long> seed(
            @ApiParam(value = "ZSet Key", required = true, example = "system:redis:zset:demo:leaderboard")
            @RequestParam(required = false) String key,
            @ApiParam(value = "造数数量", example = "10")
            @RequestParam(required = false) @Min(1) Integer count,
            @ApiParam(value = "随机分数下限", example = "0")
            @RequestParam(required = false) Integer minScore,
            @ApiParam(value = "随机分数上限", example = "100")
            @RequestParam(required = false) Integer maxScore) {

        String resolvedKey = (key == null || key.trim().isEmpty()) ? "system:redis:zset:demo:leaderboard" : key.trim();
        int resolvedCount = count == null ? 10 : count;
        int resolvedMinScore = minScore == null ? 0 : minScore;
        int resolvedMaxScore = maxScore == null ? 100 : maxScore;
        if (resolvedMaxScore < resolvedMinScore) {
            int tmp = resolvedMaxScore;
            resolvedMaxScore = resolvedMinScore;
            resolvedMinScore = tmp;
        }

        ZSetOperations<String, String> zsetOps = stringRedisTemplate.opsForZSet();
        for (int i = 1; i <= resolvedCount; i++) {
            String member = "user:" + i;
            double score = ThreadLocalRandom.current().nextInt(resolvedMinScore, resolvedMaxScore + 1);
            zsetOps.add(resolvedKey, member, score);
        }
        Long size = zsetOps.size(resolvedKey);
        log.trace("批量造数完成，ZSet Key: {}, 成员数量: {}", resolvedKey, size);
        log.debug("批量造数完成，ZSet Key: {}, 成员数量: {}", resolvedKey, size);
        log.info("批量造数完成，ZSet Key: {}, 成员数量: {}", resolvedKey, size);
        return Result.success(size == null ? 0L : size);
    }

    @ApiOperation("ZADD：添加/更新成员分数")
    @PostMapping("/zadd")
    public Result<Boolean> zadd(
            @ApiParam(value = "ZSet Key", required = true, example = "system:redis:zset:demo:leaderboard")
            @RequestParam @NotBlank String key,
            @ApiParam(value = "成员", required = true, example = "user:1")
            @RequestParam @NotBlank String member,
            @ApiParam(value = "分数", required = true, example = "88.5")
            @RequestParam @NotNull Double score) {

        Boolean added = stringRedisTemplate.opsForZSet().add(key, member, score);
        return Result.success(Boolean.TRUE.equals(added));
    }

    @ApiOperation("ZADD：批量添加/更新（entries 格式：m1=10,m2=20）")
    @PostMapping("/zadd-batch")
    public Result<Long> zaddBatch(
            @ApiParam(value = "ZSet Key", required = true, example = "system:redis:zset:demo:leaderboard")
            @RequestParam @NotBlank String key,
            @ApiParam(value = "批量条目", required = true, example = "user:1=10,user:2=20")
            @RequestParam @NotBlank String entries) {

        Set<ZSetOperations.TypedTuple<String>> tuples = new LinkedHashSet<>();
        for (String entry : splitAndTrim(entries)) {
            int idx = entry.indexOf('=');
            if (idx <= 0 || idx >= entry.length() - 1) {
                continue;
            }
            String member = entry.substring(0, idx).trim();
            String scoreStr = entry.substring(idx + 1).trim();
            if (member.isEmpty() || scoreStr.isEmpty()) {
                continue;
            }
            Double score;
            try {
                score = Double.valueOf(scoreStr);
            } catch (Exception e) {
                continue;
            }
            tuples.add(new DefaultTypedTuple<>(member, score));
        }
        if (tuples.isEmpty()) {
            return Result.success(0L);
        }
        Long addedCount = stringRedisTemplate.opsForZSet().add(key, tuples);
        return Result.success(addedCount == null ? 0L : addedCount);
    }

    @ApiOperation("ZINCRBY：增量更新成员分数")
    @PostMapping("/zincrby")
    public Result<Double> zincrby(
            @ApiParam(value = "ZSet Key", required = true, example = "system:redis:zset:demo:leaderboard")
            @RequestParam @NotBlank String key,
            @ApiParam(value = "成员", required = true, example = "user:1")
            @RequestParam @NotBlank String member,
            @ApiParam(value = "增量", required = true, example = "5")
            @RequestParam @NotNull Double delta) {

        Double newScore = stringRedisTemplate.opsForZSet().incrementScore(key, member, delta);
        return Result.success(newScore);
    }

    @ApiOperation("ZSCORE：查询成员分数")
    @GetMapping("/zscore")
    public Result<Double> zscore(
            @ApiParam(value = "ZSet Key", required = true, example = "system:redis:zset:demo:leaderboard")
            @RequestParam @NotBlank String key,
            @ApiParam(value = "成员", required = true, example = "user:1")
            @RequestParam @NotBlank String member) {

        Double score = stringRedisTemplate.opsForZSet().score(key, member);
        return Result.success(score);
    }

    @ApiOperation("ZRANK/ZREVRANK：查询成员排名（asc/desc）")
    @GetMapping("/zrank")
    public Result<Long> zrank(
            @ApiParam(value = "ZSet Key", required = true, example = "system:redis:zset:demo:leaderboard")
            @RequestParam @NotBlank String key,
            @ApiParam(value = "成员", required = true, example = "user:1")
            @RequestParam @NotBlank String member,
            @ApiParam(value = "排序：asc|desc", example = "desc")
            @RequestParam(required = false) String order) {

        Long rank = isDesc(order) ? stringRedisTemplate.opsForZSet().reverseRank(key, member) : stringRedisTemplate.opsForZSet().rank(key, member);
        return Result.success(rank);
    }

    @ApiOperation("ZCARD：成员数量")
    @GetMapping("/zcard")
    public Result<Long> zcard(
            @ApiParam(value = "ZSet Key", required = true, example = "system:redis:zset:demo:leaderboard")
            @RequestParam @NotBlank String key) {

        Long size = stringRedisTemplate.opsForZSet().size(key);
        return Result.success(size == null ? 0L : size);
    }

    @ApiOperation("ZRANGE/ZREVRANGE：按排名区间查询")
    @GetMapping("/zrange")
    public Result<List<ZsetItem>> zrange(
            @ApiParam(value = "ZSet Key", required = true, example = "system:redis:zset:demo:leaderboard")
            @RequestParam @NotBlank String key,
            @ApiParam(value = "开始下标（0-based）", required = true, example = "0")
            @RequestParam @NotNull Long start,
            @ApiParam(value = "结束下标（包含）", required = true, example = "9")
            @RequestParam @NotNull Long end,
            @ApiParam(value = "排序：asc|desc", example = "desc")
            @RequestParam(required = false) String order,
            @ApiParam(value = "是否携带分数", example = "true")
            @RequestParam(required = false) Boolean withScores) {

        boolean resolvedWithScores = Boolean.TRUE.equals(withScores);
        ZSetOperations<String, String> zsetOps = stringRedisTemplate.opsForZSet();

        if (!resolvedWithScores) {
            Set<String> members = isDesc(order) ? zsetOps.reverseRange(key, start, end) : zsetOps.range(key, start, end);
            List<ZsetItem> items = new ArrayList<>();
            if (members != null) {
                for (String m : members) {
                    items.add(new ZsetItem(m, null));
                }
            }
            return Result.success(items);
        }

        Set<ZSetOperations.TypedTuple<String>> tuples = isDesc(order) ? zsetOps.reverseRangeWithScores(key, start, end) : zsetOps.rangeWithScores(key, start, end);
        return Result.success(toItems(tuples));
    }

    @ApiOperation("ZRANGEBYSCORE/ZREVRANGEBYSCORE：按分数区间查询")
    @GetMapping("/zrange-by-score")
    public Result<List<ZsetItem>> zrangeByScore(
            @ApiParam(value = "ZSet Key", required = true, example = "system:redis:zset:demo:leaderboard")
            @RequestParam @NotBlank String key,
            @ApiParam(value = "最小分数（包含）", required = true, example = "0")
            @RequestParam @NotNull Double min,
            @ApiParam(value = "最大分数（包含）", required = true, example = "100")
            @RequestParam @NotNull Double max,
            @ApiParam(value = "排序：asc|desc", example = "desc")
            @RequestParam(required = false) String order,
            @ApiParam(value = "是否携带分数", example = "true")
            @RequestParam(required = false) Boolean withScores,
            @ApiParam(value = "偏移量（可选）", example = "0")
            @RequestParam(required = false) @Min(0) Long offset,
            @ApiParam(value = "条数（可选）", example = "10")
            @RequestParam(required = false) @Min(1) Long count) {

        boolean resolvedWithScores = Boolean.TRUE.equals(withScores);
        ZSetOperations<String, String> zsetOps = stringRedisTemplate.opsForZSet();
        boolean desc = isDesc(order);

        if (offset == null || count == null) {
            if (!resolvedWithScores) {
                Set<String> members = desc ? zsetOps.reverseRangeByScore(key, min, max) : zsetOps.rangeByScore(key, min, max);
                List<ZsetItem> items = new ArrayList<>();
                if (members != null) {
                    for (String m : members) {
                        items.add(new ZsetItem(m, null));
                    }
                }
                return Result.success(items);
            }
            Set<ZSetOperations.TypedTuple<String>> tuples = desc ? zsetOps.reverseRangeByScoreWithScores(key, min, max) : zsetOps.rangeByScoreWithScores(key, min, max);
            return Result.success(toItems(tuples));
        }

        if (!resolvedWithScores) {
            Set<String> members = desc ? zsetOps.reverseRangeByScore(key, min, max, offset, count) : zsetOps.rangeByScore(key, min, max, offset, count);
            List<ZsetItem> items = new ArrayList<>();
            if (members != null) {
                for (String m : members) {
                    items.add(new ZsetItem(m, null));
                }
            }
            return Result.success(items);
        }

        Set<ZSetOperations.TypedTuple<String>> tuples = desc ? zsetOps.reverseRangeByScoreWithScores(key, min, max, offset, count) : zsetOps.rangeByScoreWithScores(key, min, max, offset, count);
        return Result.success(toItems(tuples));
    }

    @ApiOperation("ZCOUNT：统计分数区间内成员数量")
    @GetMapping("/zcount")
    public Result<Long> zcount(
            @ApiParam(value = "ZSet Key", required = true, example = "system:redis:zset:demo:leaderboard")
            @RequestParam @NotBlank String key,
            @ApiParam(value = "最小分数（包含）", required = true, example = "0")
            @RequestParam @NotNull Double min,
            @ApiParam(value = "最大分数（包含）", required = true, example = "100")
            @RequestParam @NotNull Double max) {

        Long count = stringRedisTemplate.opsForZSet().count(key, min, max);
        return Result.success(count == null ? 0L : count);
    }

    @ApiOperation("ZREM：删除成员（members 可传逗号分隔）")
    @PostMapping("/zrem")
    public Result<Long> zrem(
            @ApiParam(value = "ZSet Key", required = true, example = "system:redis:zset:demo:leaderboard")
            @RequestParam @NotBlank String key,
            @ApiParam(value = "成员（逗号分隔）", required = true, example = "user:1,user:2")
            @RequestParam @NotBlank String members) {

        String[] memberArr = splitAndTrim(members).toArray(new String[0]);
        if (memberArr.length == 0) {
            return Result.success(0L);
        }
        Long removed = stringRedisTemplate.opsForZSet().remove(key, (Object[]) memberArr);
        return Result.success(removed == null ? 0L : removed);
    }

    @ApiOperation("ZREMRANGEBYRANK：按排名区间删除")
    @PostMapping("/zremrange-by-rank")
    public Result<Long> zremrangeByRank(
            @ApiParam(value = "ZSet Key", required = true, example = "system:redis:zset:demo:leaderboard")
            @RequestParam @NotBlank String key,
            @ApiParam(value = "开始下标（0-based）", required = true, example = "0")
            @RequestParam @NotNull Long start,
            @ApiParam(value = "结束下标（包含）", required = true, example = "0")
            @RequestParam @NotNull Long end) {

        Long removed = stringRedisTemplate.opsForZSet().removeRange(key, start, end);
        return Result.success(removed == null ? 0L : removed);
    }

    @ApiOperation("ZREMRANGEBYSCORE：按分数区间删除")
    @PostMapping("/zremrange-by-score")
    public Result<Long> zremrangeByScore(
            @ApiParam(value = "ZSet Key", required = true, example = "system:redis:zset:demo:leaderboard")
            @RequestParam @NotBlank String key,
            @ApiParam(value = "最小分数（包含）", required = true, example = "0")
            @RequestParam @NotNull Double min,
            @ApiParam(value = "最大分数（包含）", required = true, example = "10")
            @RequestParam @NotNull Double max) {

        Long removed = stringRedisTemplate.opsForZSet().removeRangeByScore(key, min, max);
        return Result.success(removed == null ? 0L : removed);
    }

    @ApiOperation("ZPOPMIN：弹出最小分数成员（可指定 count）")
    @PostMapping("/zpop-min")
    public Result<List<ZsetItem>> zpopMin(
            @ApiParam(value = "ZSet Key", required = true, example = "system:redis:zset:demo:leaderboard")
            @RequestParam @NotBlank String key,
            @ApiParam(value = "弹出数量", example = "1")
            @RequestParam(required = false) @Min(1) Long count) {

        long resolvedCount = count == null ? 1L : count;
        ZSetOperations<String, String> zsetOps = stringRedisTemplate.opsForZSet();
        Set<ZSetOperations.TypedTuple<String>> tuples = resolvedCount == 1L ? singletonOrNull(zsetOps.popMin(key)) : zsetOps.popMin(key, resolvedCount);
        return Result.success(toItems(tuples));
    }

    @ApiOperation("ZPOPMAX：弹出最大分数成员（可指定 count）")
    @PostMapping("/zpop-max")
    public Result<List<ZsetItem>> zpopMax(
            @ApiParam(value = "ZSet Key", required = true, example = "system:redis:zset:demo:leaderboard")
            @RequestParam @NotBlank String key,
            @ApiParam(value = "弹出数量", example = "1")
            @RequestParam(required = false) @Min(1) Long count) {

        long resolvedCount = count == null ? 1L : count;
        ZSetOperations<String, String> zsetOps = stringRedisTemplate.opsForZSet();
        Set<ZSetOperations.TypedTuple<String>> tuples = resolvedCount == 1L ? singletonOrNull(zsetOps.popMax(key)) : zsetOps.popMax(key, resolvedCount);
        return Result.success(toItems(tuples));
    }

    private static boolean isDesc(String order) {
        return order != null && "desc".equalsIgnoreCase(order.trim());
    }

    private static List<String> splitAndTrim(String raw) {
        List<String> items = new ArrayList<>();
        if (raw == null) {
            return items;
        }
        String[] parts = raw.split(",");
        for (String p : parts) {
            if (p == null) {
                continue;
            }
            String s = p.trim();
            if (!s.isEmpty()) {
                items.add(s);
            }
        }
        return items;
    }

    private static List<ZsetItem> toItems(Set<ZSetOperations.TypedTuple<String>> tuples) {
        List<ZsetItem> items = new ArrayList<>();
        if (tuples == null) {
            return items;
        }
        for (ZSetOperations.TypedTuple<String> t : tuples) {
            if (t == null) {
                continue;
            }
            items.add(new ZsetItem(t.getValue(), t.getScore()));
        }
        return items;
    }

    private static Set<ZSetOperations.TypedTuple<String>> singletonOrNull(ZSetOperations.TypedTuple<String> tuple) {
        if (tuple == null) {
            return null;
        }
        Set<ZSetOperations.TypedTuple<String>> set = new LinkedHashSet<>();
        set.add(tuple);
        return set;
    }

    public static class ZsetItem {
        private final String member;
        private final Double score;

        public ZsetItem(String member, Double score) {
            this.member = member;
            this.score = score;
        }

        public String getMember() {
            return member;
        }

        public Double getScore() {
            return score;
        }
    }
}
