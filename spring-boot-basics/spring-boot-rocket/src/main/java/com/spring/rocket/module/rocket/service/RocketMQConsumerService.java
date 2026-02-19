package com.spring.rocket.module.rocket.service;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

/**
 * RocketMQ 消费者服务
 *
 * @author qujianlei
 * @since 1.0.0
 */
@Service
@RocketMQMessageListener(topic = "test-topic", consumerGroup = "rocket-consumer-group")
public class RocketMQConsumerService implements RocketMQListener<String> {
    
    @Override
    public void onMessage(String message) {
        System.out.println("rocket-consumer-group 接收到 RocketMQ 消息: " + message);
    }
}
