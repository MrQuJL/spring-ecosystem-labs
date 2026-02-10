package com.spring.minimal.module.customer.service.impl;

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
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

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
    public Page<CustomerVO> pageList(CustomerPageQuery query) {
        // 1. 构建分页对象
        Page<Customer> page = new Page<>(query.getPage(), query.getSize());
        
        // 2. 执行查询 (返回的是 PO)
        this.lambdaQuery()
                .like(StringUtils.isNotBlank(query.getName()), Customer::getName, query.getName())
                .eq(query.getStatus() != null, Customer::getStatus, query.getStatus())
                .orderByDesc(Customer::getId)
                .page(page);

        // 3. 转换: PO -> VO
        List<CustomerVO> voList = page.getRecords().stream()
                .map(CustomerVO::fromCustomer)
                .collect(Collectors.toList());

        // 4. 构建新的 Page<VO>
        Page<CustomerVO> voPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        voPage.setRecords(voList);

        return voPage;
    }
}
