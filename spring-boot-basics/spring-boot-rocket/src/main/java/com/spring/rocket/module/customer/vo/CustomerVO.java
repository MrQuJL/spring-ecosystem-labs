package com.spring.rocket.module.customer.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 客户展示对象 (View Object)
 * <p>用于前端展示，屏蔽敏感字段或转换展示格式</p>
 *
 * @author qujianlei
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

    @Schema(description = "状态: 0-冻结, 1-正常", example = "正常")
    private String statusDesc;

    @Schema(description = "创建时间", example = "2023-10-27 10:00:00")
    private LocalDateTime createdAt;
    
    @Schema(description = "更新时间", example = "2023-10-28 10:00:00")
    private LocalDateTime updatedAt;
}
