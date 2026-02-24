package com.spring.elastic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ElasticSearch 启动类
 * <p>Spring Boot 应用的入口点</p>
 * 
 * @author qujianlei
 * @since 1.0.0
 */
@SpringBootApplication
public class ElasticSearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(ElasticSearchApplication.class, args);
    }
}
