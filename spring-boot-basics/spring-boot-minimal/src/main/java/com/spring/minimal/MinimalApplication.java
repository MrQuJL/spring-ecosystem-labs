package com.spring.minimal;

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
@MapperScan("com.spring.minimal.module.*.mapper")
public class MinimalApplication {

    public static void main(String[] args) {
        SpringApplication.run(MinimalApplication.class, args);
    }
}
