package com.spring.mongo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 启动类
 * <p>Spring Boot 应用的入口点</p>
 * 
 * @author qujianlei
 * @since 1.0.0
 */
@SpringBootApplication
@MapperScan("com.spring.mongo.module.*.mapper")
public class MongoApplication {

    public static void main(String[] args) {
        SpringApplication.run(MongoApplication.class, args);
    }
}
