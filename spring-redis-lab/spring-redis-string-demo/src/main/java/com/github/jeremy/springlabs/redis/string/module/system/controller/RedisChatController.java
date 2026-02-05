package com.github.jeremy.springlabs.redis.string.module.system.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.jeremy.springlabs.redis.string.common.result.Result;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/redis-chat")
@Api(tags = "Redis聊天室测试接口")
@Validated
@Slf4j
public class RedisChatController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    // 线程池，用于异步执行数据库操作
    private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(5);

    // 模拟数据库存储（实际项目中应使用真实数据库）
    private static final Map<String, List<ChatMessage>> CHAT_DB = new HashMap<>();

    // 每条消息的最大长度
    private static final int MAX_MESSAGE_LENGTH = 500;

    // 每个聊天室保留的最大消息数
    private static final int MAX_MESSAGES_PER_ROOM = 50;

    // 聊天室消息的过期时间（秒）
    private static final long MESSAGE_EXPIRE_TIME = 3600;

    /**
     * 向指定聊天室发送消息
     * @param roomId 聊天室ID
     * @param userId 用户ID
     * @param content 消息内容
     * @return 发送结果
     */
    @ApiOperation("向指定聊天室发送消息")
    @PostMapping("/send")
    public Result<Boolean> sendMessage(
            @ApiParam(value = "聊天室ID", required = true, example = "room:1001")
            @RequestParam @NotBlank String roomId,
            @ApiParam(value = "用户ID", required = true, example = "user:101")
            @RequestParam @NotBlank String userId,
            @ApiParam(value = "消息内容", required = true, example = "Hello, Redis Chat!")
            @RequestParam @NotBlank @Size(max = MAX_MESSAGE_LENGTH) String content) {

        // 生成消息ID和时间戳
        String messageId = "msg:" + System.currentTimeMillis();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        // 构造消息内容（格式：时间戳|用户ID|消息内容）
        String message = timestamp + "|" + userId + "|" + content;

        // 1. 存入 Redis List（右侧添加）
        String redisKey = "chat:" + roomId + ":messages";
        stringRedisTemplate.opsForList().rightPush(redisKey, message);

        // 2. 修剪 List，只保留最近 50 条消息
        stringRedisTemplate.opsForList().trim(redisKey, -50, -1);

        // 3. 设置过期时间（1小时）
        stringRedisTemplate.expire(redisKey, MESSAGE_EXPIRE_TIME, TimeUnit.SECONDS);

        // 4. 异步存入模拟数据库（HashMap）
        EXECUTOR_SERVICE.execute(() -> {
            CHAT_DB.computeIfAbsent(roomId, k -> new ArrayList<>()).add(
                    new ChatMessage(messageId, roomId, userId, content, timestamp)
            );
            log.info("异步存入数据库：聊天室={}, 用户={}, 消息ID={}", roomId, userId, messageId);
        });

        log.info("向聊天室 {} 发送消息: 用户={}, 内容={}", roomId, userId, content);
        return Result.success(Boolean.TRUE);
    }

    /**
     * 获取聊天室最近的50条消息
     * @param roomId 聊天室ID
     * @return 消息列表
     */
    @ApiOperation("获取聊天室最近的50条消息")
    @GetMapping("/messages")
    public Result<List<String>> getMessages(
            @ApiParam(value = "聊天室ID", required = true, example = "room:1001")
            @RequestParam @NotBlank String roomId) {

        // 从 Redis List 中获取最近 50 条消息
        String redisKey = "chat:" + roomId + ":messages";
        List<String> messages = stringRedisTemplate.opsForList().range(redisKey, -50, -1);

        // 如果 Redis 中没有数据，返回空列表
        if (messages == null) {
            messages = new ArrayList<>();
        }

        log.info("获取聊天室 {} 的消息，共 {} 条", roomId, messages.size());
        return Result.success(messages);
    }

    /**
     * 聊天消息实体类（用于模拟数据库存储）
     */
    @Getter
    private static class ChatMessage {
        private final String id;
        private final String roomId;
        private final String userId;
        private final String content;
        private final String timestamp;

        public ChatMessage(String id, String roomId, String userId, String content, String timestamp) {
            this.id = id;
            this.roomId = roomId;
            this.userId = userId;
            this.content = content;
            this.timestamp = timestamp;
        }

    }
}
