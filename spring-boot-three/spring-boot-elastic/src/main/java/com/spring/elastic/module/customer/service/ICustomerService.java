package com.spring.elastic.module.customer.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.spring.elastic.module.customer.dto.CustomerDTO;
import com.spring.elastic.module.customer.dto.CustomerPageQuery;
import com.spring.elastic.module.customer.dto.CustomerStatusReq;
import com.spring.elastic.module.customer.entity.Customer;
import com.spring.elastic.module.customer.vo.CustomerVO;

import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletResponse;

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
     * 删除客户
     *
     * @param id 客户ID
     * @return 是否成功
     */
    boolean deleteCustomer(Long id);

    /**
     * 获取客户详情
     *
     * @param id 客户ID
     * @return 客户详情(VO)
     */
    CustomerVO getCustomer(Long id);

    /**
     * 分页查询客户
     *
     * @param query 查询条件
     * @return 分页结果(VO)
     */
    IPage<CustomerVO> pageList(CustomerPageQuery query);
    
    /**
     * 导出客户数据
     *
     * @param response 响应对象
     * @param status   客户状态
     */
    void exportCustomer(HttpServletResponse response, Integer status);

    /**
     * 导入客户数据
     *
     * @param file          Excel文件
     * @param operatorName  操作人名称
     * @param operatorId    操作人ID
     */
    void importCustomer(MultipartFile file, String operatorName, Long operatorId);
}
