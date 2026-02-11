package com.spring.minimal.module.customer.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spring.minimal.module.customer.dto.CustomerDTO;
import com.spring.minimal.module.customer.dto.CustomerPageQuery;
import com.spring.minimal.module.customer.dto.CustomerStatusReq;
import com.spring.minimal.module.customer.entity.Customer;
import com.spring.minimal.module.customer.enums.business.CustomerStatusEnum;
import com.spring.minimal.module.customer.mapper.CustomerMapper;
import com.spring.minimal.module.customer.service.ICustomerService;
import com.spring.minimal.module.customer.vo.CustomerVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * 客户服务实现类
 *
 * @author qujianlei
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements ICustomerService {

    private final ModelMapper modelMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addCustomer(CustomerDTO customerDTO) {
        Customer customer = modelMapper.map(customerDTO, Customer.class);
        
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
        modelMapper.map(customerDTO, customer);
        
        // 注意：这里显式忽略了 balance 和 status 的更新，确保安全
        
        return this.updateById(customer);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateStatus(CustomerStatusReq req) {
        // 校验状态值是否有效
        boolean isValidStatus = false;
        for (CustomerStatusEnum value : CustomerStatusEnum.values()) {
            if (value.getCode().equals(req.getStatus())) {
                isValidStatus = true;
                break;
            }
        }
        if (!isValidStatus) {
            throw new IllegalArgumentException("无效的状态码: " + req.getStatus());
        }

        Customer customer = this.getById(req.getId());
        if (customer == null) {
            throw new RuntimeException("客户不存在");
        }

        customer.setStatus(req.getStatus());
        return this.updateById(customer);
    }

    @Override
    public IPage<CustomerVO> pageList(CustomerPageQuery query) {
        // 1. 构建分页对象
        Page<Customer> pageParam = new Page<>(query.getPage(), query.getSize());
        
        // 2. 执行查询并转换 (PO -> VO)
        IPage<Customer> customerPage = this.lambdaQuery()
                .like(StringUtils.isNotBlank(query.getName()), Customer::getName, query.getName())
                .eq(query.getStatus() != null, Customer::getStatus, query.getStatus())
                .orderByDesc(Customer::getId)
                .page(pageParam);
        
        // 3. 转换状态描述
        IPage<CustomerVO> customerVOPage = customerPage.convert(customer -> {
            CustomerVO customerVO = modelMapper.map(customer, CustomerVO.class);
            customerVO.setStatusDesc(CustomerStatusEnum.getByCode(customer.getStatus()).getDesc());
            return customerVO;
        });
        
        return customerVOPage;
    }
}
