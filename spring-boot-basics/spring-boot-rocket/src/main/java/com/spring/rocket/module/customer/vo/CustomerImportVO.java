package com.spring.rocket.module.customer.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 客户导入 VO
 *
 * @author qujianlei
 * @since 1.0.0
 */
@Data
public class CustomerImportVO {

    @ExcelProperty("客户名称")
    private String name;

    @ExcelProperty("账户余额")
    private BigDecimal balance;

    @ExcelProperty("状态")
    private String statusDesc;
}
