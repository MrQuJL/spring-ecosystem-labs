package com.spring.minimal.module.customer.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.spring.minimal.module.customer.dto.CustomerDTO;
import com.spring.minimal.module.customer.dto.CustomerPageQuery;
import com.spring.minimal.module.customer.dto.CustomerStatusReq;
import com.spring.minimal.module.customer.entity.Customer;
import com.spring.minimal.module.customer.vo.CustomerVO;

/**
 * 客户服务接口
 *
 * @author qujianlei
 * @since 1.0.0
 */
public interface ICustomerService extends IService<Customer> {

    /**
     * 新增客户
     *
     * @param customerDTO 客户信息
     * @return 是否成功
     */
    boolean addCustomer(CustomerDTO customerDTO);

    /**
     * 更新客户信息
     *
     * @param customerDTO 客户信息
     * @return 是否成功
     */
    boolean updateCustomer(CustomerDTO customerDTO);

    /**
     * 更新客户状态
     *
     * @param req 状态更新请求
     * @return 是否成功
     */
    boolean updateStatus(CustomerStatusReq req);

    /**
     * 分页查询客户
     *
     * @param query 查询条件
     * @return 分页结果(VO)
     */
    IPage<CustomerVO> pageList(CustomerPageQuery query);
}
