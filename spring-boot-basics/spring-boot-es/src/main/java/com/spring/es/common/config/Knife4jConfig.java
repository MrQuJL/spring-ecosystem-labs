package com.spring.es.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Knife4jConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Elasticsearch Demo API")
                        .version("1.0.0")
                        .description("Elasticsearch 增删改查 Demo")
                        .contact(new Contact()
                                .name("qujianlei")));
    }
}
