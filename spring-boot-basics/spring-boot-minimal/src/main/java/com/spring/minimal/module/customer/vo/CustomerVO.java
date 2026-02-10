package com.spring.minimal.module.customer.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.spring.minimal.module.customer.entity.Customer;
import com.spring.minimal.module.customer.enums.business.CustomerStatusEnum;
import org.springframework.beans.BeanUtils;

/**
 * 客户展示对象 (View Object)
 * <p>用于前端展示，屏蔽敏感字段或转换展示格式</p>
 *
 * @author spring-minimal
 * @since 1.0.0
 */
@Data
@Schema(description = "客户展示信息")
public class CustomerVO {

    @Schema(description = "客户ID", example = "1001")
    private Long id;

    @Schema(description = "客户名称", example = "张三")
    private String name;

    @Schema(description = "账户余额", example = "100.00")
    private BigDecimal balance;

    @Schema(description = "状态: 0-冻结, 1-正常", example = "1")
    private String status;

    @Schema(description = "创建时间", example = "2023-10-27 10:00:00")
    private LocalDateTime createdAt;
    
    @Schema(description = "更新时间", example = "2023-10-28 10:00:00")
    private LocalDateTime updatedAt;

    /**
     * 将 Customer 实体对象转换为 CustomerVO 展示对象
     *
     * @param customer 客户实体
     * @return CustomerVO 展示对象
     */
    public static CustomerVO fromCustomer(Customer customer) {
        if (customer == null) {
            return null;
        }
        CustomerVO vo = new CustomerVO();
        // 1. 复制同名且类型匹配的属性 (id, name, balance, createdAt, updatedAt)
        BeanUtils.copyProperties(customer, vo);
        
        // 2. 手动处理类型不匹配或需要转换的属性 (status: Integer -> String)
        if (customer.getStatus() != null) {
            vo.setStatus(CustomerStatusEnum.getByCode(customer.getStatus()).getDesc());
        }
        
        return vo;
    }
}
