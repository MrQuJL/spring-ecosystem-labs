package com.spring.mongo.module.customer.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spring.mongo.common.enums.business.BaseEnum;
import com.spring.mongo.common.enums.response.ResponseEnum;
import com.spring.mongo.common.exception.BusinessException;
import com.spring.mongo.common.utils.ExcelUtil;
import com.spring.mongo.module.customer.dto.CustomerDTO;
import com.spring.mongo.module.customer.dto.CustomerPageQuery;
import com.spring.mongo.module.customer.dto.CustomerStatusReq;
import com.spring.mongo.module.customer.entity.Customer;
import com.spring.mongo.module.customer.enums.business.CustomerStatusEnum;
import com.spring.mongo.module.customer.enums.response.CustomerResponseEnum;
import com.spring.mongo.module.customer.mapper.CustomerMapper;
import com.spring.mongo.module.customer.service.ICustomerService;
import com.spring.mongo.module.customer.vo.CustomerExcelVO;
import com.spring.mongo.module.customer.vo.CustomerImportVO;
import com.spring.mongo.module.customer.vo.CustomerVO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

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
        // 校验客户名称不能重复
        long count = this.lambdaQuery()
                .eq(Customer::getName, customerDTO.getName())
                .count();
        if (count > 0) {
            throw new BusinessException(CustomerResponseEnum.CUSTOMER_NAME_EXIST,
                String.format(CustomerResponseEnum.CUSTOMER_NAME_EXIST.getMessage(), customerDTO.getName()));
        }

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
            throw new BusinessException(CustomerResponseEnum.CUSTOMER_NOT_EXIST,
                    String.format(CustomerResponseEnum.CUSTOMER_NOT_EXIST.getMessage(), customerDTO.getId()));
        }

        // 校验客户名称不能重复
        long count = this.lambdaQuery()
                .eq(Customer::getName, customerDTO.getName())
                .ne(Customer::getId, customerDTO.getId())
                .count();
        if (count > 0) {
            throw new BusinessException(CustomerResponseEnum.CUSTOMER_NAME_EXIST,
                String.format(CustomerResponseEnum.CUSTOMER_NAME_EXIST.getMessage(), customerDTO.getName()));
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
        boolean isValidStatus = BaseEnum.isValidCode(CustomerStatusEnum.class, req.getStatus());
        if (!isValidStatus) {
            throw new BusinessException(CustomerResponseEnum.STATUS_INVALID, 
                String.format(CustomerResponseEnum.STATUS_INVALID.getMessage(), req.getStatus()));
        }

        Customer customer = this.getById(req.getId());
        if (customer == null) {
            throw new BusinessException(CustomerResponseEnum.CUSTOMER_NOT_EXIST,
                String.format(CustomerResponseEnum.CUSTOMER_NOT_EXIST.getMessage(), req.getId()));
        }

        customer.setStatus(req.getStatus());
        return this.updateById(customer);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteCustomer(Long id) {
        Customer customer = this.getById(id);
        if (customer == null) {
            throw new BusinessException(CustomerResponseEnum.CUSTOMER_NOT_EXIST,
                String.format(CustomerResponseEnum.CUSTOMER_NOT_EXIST.getMessage(), id));
        }
        return this.removeById(id);
    }

    @Override
    public CustomerVO getCustomer(Long id) {
        Customer customer = this.getById(id);
        if (customer == null) {
            throw new BusinessException(CustomerResponseEnum.CUSTOMER_NOT_EXIST,
                String.format(CustomerResponseEnum.CUSTOMER_NOT_EXIST.getMessage(), id));
        }
        CustomerVO customerVO = modelMapper.map(customer, CustomerVO.class);
        // 转换状态描述
        String statusDesc = BaseEnum.getByCode(CustomerStatusEnum.class, customer.getStatus()).getDesc();
        customerVO.setStatusDesc(statusDesc);

        return customerVO;
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
            String statusDesc = BaseEnum.getByCode(CustomerStatusEnum.class, customer.getStatus()).getDesc();
            customerVO.setStatusDesc(statusDesc);
            return customerVO;
        });
        
        return customerVOPage;
    }

    @Override
    public void exportCustomer(HttpServletResponse response, Integer status) {
        // 1. 查询数据
        List<Customer> customerList = this.lambdaQuery()
                .eq(status != null, Customer::getStatus, status)
                .orderByDesc(Customer::getId)
                .list();

        // 2. 转换数据 (PO -> ExcelVO)
        List<CustomerExcelVO> excelData = customerList.stream().map(customer -> {
            CustomerExcelVO excelVO = modelMapper.map(customer, CustomerExcelVO.class);
            String statusDesc = BaseEnum.getByCode(CustomerStatusEnum.class, customer.getStatus()).getDesc();
            excelVO.setStatusDesc(statusDesc);
            return excelVO;
        }).collect(Collectors.toList());

        // 3. 导出
        ExcelUtil.write(response, "客户列表", "客户数据", CustomerExcelVO.class, excelData);
    }

    @Override
    public void importCustomer(MultipartFile file, String operatorName, Long operatorId) {
        try {
            ExcelUtil.read(file.getInputStream(), CustomerImportVO.class, list -> {
                // 仅打印数据
                log.info("================== 开始导入 ==================");
                log.info("操作人: {}, ID: {}", operatorName, operatorId);
                log.info("导入数据量: {}", list.size());
                list.forEach(item -> log.info("导入数据: {}", item));
                log.info("================== 结束导入 ==================");
            });
            // List<CustomerImportVO> importData = ExcelUtil.readSync(file.getInputStream(), CustomerImportVO.class);
            // System.out.println(importData);
        } catch (IOException e) {
            log.error("导入失败", e);
            throw new BusinessException(ResponseEnum.SYSTEM_ERROR, "导入文件读取失败");
        }
    }
}