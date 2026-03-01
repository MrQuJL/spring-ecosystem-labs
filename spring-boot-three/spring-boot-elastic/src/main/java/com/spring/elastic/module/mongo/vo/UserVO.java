package com.spring.elastic.module.mongo.vo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 用户展示对象 (View Object)
 * <p>用于前端展示，屏蔽敏感字段或转换展示格式</p>
 *
 * @author qujianlei
 * @since 1.0.0
 */
@Data
@Schema(description = "用户展示对象")
public class UserVO {
    
    @Schema(description = "用户ID", example = "6997e7948b6117674b5d3aa9")
    private String id;

    @Schema(description = "用户名称", example = "张三")
    private String name;

    @Schema(description = "用户年龄", example = "18")
    private Long age;

    @Schema(description = "用户邮箱", example = "zhangsan@example.com")
    private String email;

    @Schema(description = "账户余额", example = "1000.00")
    private BigDecimal balance;

    @Schema(description = "用户状态", example = "正常")
    private String statusStr;

    @Schema(description = "创建时间", example = "2023-01-01 00:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "更新时间", example = "2023-01-01 00:00:00")
    private LocalDateTime updatedAt;
}
