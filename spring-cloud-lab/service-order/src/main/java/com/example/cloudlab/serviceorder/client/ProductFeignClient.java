package com.example.cloudlab.serviceorder.client;

import java.util.Map;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "service-product")
public interface ProductFeignClient {

    @GetMapping("/products/{productId}")
    Map<String, Object> getProduct(@PathVariable("productId") Long productId);

    @GetMapping("/products/ping")
    Map<String, Object> ping();
}
