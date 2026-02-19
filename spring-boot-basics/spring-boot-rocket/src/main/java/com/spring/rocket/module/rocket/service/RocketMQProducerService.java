package com.spring.rocket.module.rocket.service;

import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * RocketMQ 生产者服务
 *
 * @author qujianlei
 * @since 1.0.0
 */
@Service
public class RocketMQProducerService {
    
    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    
    /**
     * 发送消息到指定主题
     * 
     * @param topic 主题名称
     * @param message 消息内容
     */
    public void sendMessage(String topic, String message) {
        rocketMQTemplate.convertAndSend(topic, message);
    }
    
    /**
     * 发送消息到指定主题和标签
     * 
     * @param topic 主题名称
     * @param tag 标签名称
     * @param message 消息内容
     */
    public void sendMessageWithTag(String topic, String tag, String message) {
        rocketMQTemplate.convertAndSend(topic + ":" + tag, message);
    }
}
