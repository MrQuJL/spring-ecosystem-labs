package com.spring.elastic.module.elastic.service;

import com.spring.elastic.module.elastic.dto.ProductDTO;
import com.spring.elastic.module.elastic.dto.ProductPageQuery;
import com.spring.elastic.module.elastic.vo.ProductVO;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 商品服务接口
 *
 * @author qujianlei
 * @since 1.0.0
 */
public interface IProductService {

    /**
     * 新增商品
     *
     * @param productDTO 商品信息
     * @return 是否成功
     */
    boolean addProduct(ProductDTO productDTO);

    /**
     * 更新商品
     *
     * @param productDTO 商品信息
     * @return 是否成功
     */
    boolean updateProduct(ProductDTO productDTO);

    /**
     * 删除商品
     *
     * @param id 商品ID
     * @return 是否成功
     */
    boolean deleteProduct(String id);

    /**
     * 获取商品详情
     *
     * @param id 商品ID
     * @return 商品信息
     */
    ProductVO getProductDetail(String id);

    /**
     * 分页查询商品
     *
     * @param query 查询条件
     * @return 分页结果
     */
    Page<ProductVO> pageList(ProductPageQuery query);

    /**
     * 批量生成测试数据
     *
     * @return 生成的商品列表
     */
    List<ProductVO> batchInsertTestData();
}
