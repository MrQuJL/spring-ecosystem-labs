package com.spring.elastic.module.elastic.service.impl;

import com.spring.elastic.common.exception.BusinessException;
import com.spring.elastic.module.elastic.dto.ProductDTO;
import com.spring.elastic.module.elastic.dto.ProductPageQuery;
import com.spring.elastic.module.elastic.entity.Product;
import com.spring.elastic.module.elastic.enums.response.ProductResponseEnum;
import com.spring.elastic.module.elastic.repository.ProductRepository;
import com.spring.elastic.module.elastic.service.IProductService;
import com.spring.elastic.module.elastic.vo.ProductVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 商品服务实现类
 *
 * @author qujianlei
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements IProductService {

    private final ProductRepository productRepository;
    private final ElasticsearchOperations elasticsearchOperations;
    private final ModelMapper modelMapper;

    @Override
    public boolean addProduct(ProductDTO productDTO) {
        Product product = modelMapper.map(productDTO, Product.class);
        if (product.getCreatedAt() == null) {
            product.setCreatedAt(LocalDateTime.now());
        }
        // 生成随机ID (ES _id)
        if (!StringUtils.hasText(product.getId())) {
            product.setId(UUID.randomUUID().toString());
        }
        productRepository.save(product);
        return true;
    }

    @Override
    public boolean updateProduct(ProductDTO productDTO) {
        Product product = productRepository.findById(productDTO.getId())
                .orElseThrow(() -> new BusinessException(ProductResponseEnum.PRODUCT_NOT_EXIST, 
                    String.format(ProductResponseEnum.PRODUCT_NOT_EXIST.getMessage(), productDTO.getId())));
        
        // 更新字段
        modelMapper.map(productDTO, product);
        productRepository.save(product);
        return true;
    }

    @Override
    public boolean deleteProduct(String id) {
        if (!productRepository.existsById(id)) {
             throw new BusinessException(ProductResponseEnum.PRODUCT_NOT_EXIST, 
                 String.format(ProductResponseEnum.PRODUCT_NOT_EXIST.getMessage(), id));
        }
        productRepository.deleteById(id);
        return true;
    }

    @Override
    public ProductVO getProductDetail(String id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ProductResponseEnum.PRODUCT_NOT_EXIST, 
                    String.format(ProductResponseEnum.PRODUCT_NOT_EXIST.getMessage(), id)));
        return modelMapper.map(product, ProductVO.class);
    }

    @Override
    public Page<ProductVO> pageList(ProductPageQuery query) {
        Criteria criteria = new Criteria();
        if (StringUtils.hasText(query.getProductName())) {
            criteria.and(new Criteria("productName").contains(query.getProductName()));
        }
        if (StringUtils.hasText(query.getCategory())) {
            criteria.and(new Criteria("category").is(query.getCategory()));
        }
        if (query.getIsOnSale() != null) {
            criteria.and(new Criteria("isOnSale").is(query.getIsOnSale()));
        }
        
        if (query.getMinPrice() != null || query.getMaxPrice() != null) {
            Criteria priceCriteria = new Criteria("price");
            if (query.getMinPrice() != null) {
                priceCriteria.greaterThanEqual(query.getMinPrice());
            }
            if (query.getMaxPrice() != null) {
                priceCriteria.lessThanEqual(query.getMaxPrice());
            }
            criteria.and(priceCriteria);
        }

        Pageable pageable = PageRequest.of(query.getPage() - 1, query.getSize());
        Query searchQuery = new CriteriaQuery(criteria).setPageable(pageable);
        
        SearchHits<Product> searchHits = elasticsearchOperations.search(searchQuery, Product.class);
        
        List<ProductVO> list = searchHits.getSearchHits().stream()
                .map(hit -> modelMapper.map(hit.getContent(), ProductVO.class))
                .collect(Collectors.toList());

        return new PageImpl<>(list, pageable, searchHits.getTotalHits());
    }

    @Override
    public List<ProductVO> batchInsertTestData() {
        List<Product> productList = new ArrayList<>();
        Random random = new Random();

        String[] brands = {"Apple", "华为", "小米", "OPPO", "vivo", "三星", "索尼", "联想"};
        String[] categories = {"手机", "电脑", "平板", "手表", "耳机"};
        String[] adjectives = {"Pro", "Max", "Ultra", "Plus", "SE", "Lite", "至尊版", "旗舰版"};
        String[] baseNames = {"iPhone", "Mate", "Mi", "Find", "X", "Galaxy", "Xperia", "ThinkPad"};

        long startProductId = 2000L; 

        for (int i = 0; i < 10; i++) {
            String brand = brands[random.nextInt(brands.length)];
            String category = categories[random.nextInt(categories.length)];
            String baseName = baseNames[random.nextInt(baseNames.length)];
            String adj = adjectives[random.nextInt(adjectives.length)];
            
            String productName = baseName + " " + (10 + random.nextInt(15)) + " " + adj;
            
            double price = 1000.00 + random.nextDouble() * 14000.00;
            price = Math.round(price * 100.0) / 100.0;

            int stock = random.nextInt(501);
            boolean isOnSale = random.nextDouble() > 0.2;

            Product product = Product.builder()
                    .id(UUID.randomUUID().toString())
                    .productId(startProductId + i)
                    .productName(productName)
                    .brand(brand)
                    .category(category)
                    .price(price)
                    .stock(stock)
                    .isOnSale(isOnSale)
                    .createdAt(LocalDateTime.now().minusDays(random.nextInt(30)))
                    .build();

            productList.add(product);
        }

        Iterable<Product> savedProducts = productRepository.saveAll(productList);
        
        List<ProductVO> result = new ArrayList<>();
        savedProducts.forEach(p -> result.add(modelMapper.map(p, ProductVO.class)));
        return result;
    }
}
