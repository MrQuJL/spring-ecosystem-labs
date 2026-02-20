package com.spring.mongo.module.customer.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentStyle;
import com.alibaba.excel.enums.poi.HorizontalAlignmentEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 客户导出 VO
 *
 * @author qujianlei
 * @since 1.0.0
 */
@Data
@ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER)
public class CustomerExcelVO {

    @ExcelProperty("客户ID")
    @ColumnWidth(15)
    private Long id;

    @ExcelProperty("客户名称")
    @ColumnWidth(20)
    private String name;

    @ExcelProperty("账户余额")
    @ColumnWidth(20)
    private BigDecimal balance;

    @ExcelProperty("状态")
    @ColumnWidth(15)
    private String statusDesc;

    @ExcelProperty("创建时间")
    @ColumnWidth(25)
    private LocalDateTime createdAt;
}
