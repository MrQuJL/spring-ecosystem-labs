package com.spring.rocket.module.rocket.controller;

import com.spring.rocket.common.result.Result;
import com.spring.rocket.module.rocket.service.RocketMQProducerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * RocketMQ测试控制器
 * 提供测试发送RocketMQ消息的接口
 * 
 * @author qujianlei
 * @since 1.0.0
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/rocketmq")
@RequiredArgsConstructor
@Tag(name = "RocketMQ测试", description = "测试发送RocketMQ消息的接口")
public class RocketMQTestController {
    
    private final RocketMQProducerService rocketMQProducerService;
    
    /**
     * 测试发送RocketMQ消息
     * 
     * @param message 消息内容
     * @return 发送结果
     */
    @Operation(summary = "发送RocketMQ消息")
    @GetMapping("/send")
    public Result<String> sendMessage(@RequestParam String message) {
        rocketMQProducerService.sendMessage("test-topic", message);
        return Result.success("消息发送成功");
    }
    
    /**
     * 测试发送带标签的RocketMQ消息
     * 
     * @param tag 标签
     * @param message 消息内容
     * @return 发送结果
     */
    @Operation(summary = "发送带标签的RocketMQ消息")
    @GetMapping("/sendWithTag")
    public Result<String> sendMessageWithTag(@RequestParam String tag, @RequestParam String message) {
        rocketMQProducerService.sendMessageWithTag("test-topic", tag, message);
        return Result.success("带标签的消息发送成功");
    }
}
