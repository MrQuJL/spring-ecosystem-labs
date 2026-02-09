package com.spring.minimal.module.customer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spring.minimal.module.customer.dto.CustomerDTO;
import com.spring.minimal.module.customer.entity.Customer;
import com.spring.minimal.module.customer.enums.business.CustomerStatusEnum;
import com.spring.minimal.module.customer.mapper.CustomerMapper;
import com.spring.minimal.module.customer.service.ICustomerService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * 客户服务实现类
 *
 * @author qujianlei
 * @since 1.0.0
 */
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements ICustomerService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addCustomer(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDTO, customer);
        
        // 强制初始化逻辑
        customer.setBalance(BigDecimal.ZERO); // 初始余额为0
        customer.setStatus(CustomerStatusEnum.NORMAL.getCode()); // 初始状态为正常
        
        return this.save(customer);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateCustomer(CustomerDTO customerDTO) {
        Customer customer = this.getById(customerDTO.getId());
        if (customer == null) {
            throw new RuntimeException("客户不存在");
        }
        
        // 只更新允许修改的字段
        customer.setName(customerDTO.getName());
        
        // 注意：这里显式忽略了 balance 和 status 的更新，确保安全
        
        return this.updateById(customer);
    }
}
