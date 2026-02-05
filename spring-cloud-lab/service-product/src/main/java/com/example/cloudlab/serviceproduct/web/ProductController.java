package com.example.cloudlab.serviceproduct.web;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductController {

    @GetMapping("/ping")
    public Map<String, Object> ping() {
        return Map.of("service", "service-product", "status", "ok");
    }

    @GetMapping("/{productId}")
    public Map<String, Object> getProduct(@PathVariable Long productId) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("productId", productId);
        payload.put("name", "Demo Product");
        payload.put("price", new BigDecimal("99.00"));
        payload.put("stock", 999);
        return payload;
    }
}
