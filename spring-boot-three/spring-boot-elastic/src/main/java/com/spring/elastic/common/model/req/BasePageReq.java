package com.spring.elastic.common.model.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

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
    private Integer page = 1;

    @Schema(description = "每页条数", example = "10")
    private Integer size = 10;
}
