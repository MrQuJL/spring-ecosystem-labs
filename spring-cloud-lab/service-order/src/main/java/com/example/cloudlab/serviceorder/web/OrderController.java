package com.example.cloudlab.serviceorder.web;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.cloudlab.serviceorder.client.ProductFeignClient;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final ProductFeignClient productFeignClient;

    public OrderController(ProductFeignClient productFeignClient) {
        this.productFeignClient = productFeignClient;
    }

    @GetMapping("/ping")
    public Map<String, Object> ping() {
        return Map.of("service", "service-order", "status", "ok");
    }

    @GetMapping("/{orderId}")
    public Map<String, Object> getOrder(@PathVariable Long orderId) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("orderId", orderId);
        payload.put("productId", 1001L);
        payload.put("amount", new BigDecimal("99.00"));
        payload.put("status", "CREATED");

        // Call remote product service
        try {
            Map<String, Object> product = productFeignClient.getProduct(1001L);
            payload.put("productInfo", product);
        } catch (Exception e) {
            payload.put("productInfo", "Remote service unavailable: " + e.getMessage());
        }

        return payload;
    }

    @GetMapping("/test-remote-ping")
    public Map<String, Object> testRemotePing() {
        return productFeignClient.ping();
    }
}
