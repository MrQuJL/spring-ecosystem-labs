package com.spring.es.module.es.repository;

import com.spring.es.module.es.entity.Product;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends ElasticsearchRepository<Product, String> {

    List<Product> findByNameContaining(String name);

    List<Product> findByCategory(String category);

    List<Product> findByBrand(String brand);
}
