package com.spring.minimal.common.model.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Min;

/**
 * 分页请求基类
 * <p>包含分页参数：当前页码和每页条数</p>
 *
 * @author qujianlei
 * @since 1.0.0
 */
@Data
public class BasePageReq {

    @Schema(description = "当前页码", example = "1")
    @Min(value = 1, message = "当前页码必须大于0")
    private Integer page = 1;

    @Schema(description = "每页条数", example = "10")
    @Min(value = 1, message = "每页条数必须大于0")
    private Integer size = 10;
}
