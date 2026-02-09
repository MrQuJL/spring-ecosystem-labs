package com.spring.minimal.module.customer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.spring.minimal.module.customer.dto.CustomerDTO;
import com.spring.minimal.module.customer.entity.Customer;

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
}
