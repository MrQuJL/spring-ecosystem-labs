package com.spring.minimal.common.enums.response;

/**
 * 提示性枚举通用接口
 * 
 * @author qujianlei
 * @since 1.0.0
 */
public interface IResponseEnum {
    
    /**
     * 获取状态码
     * @return 状态码
     */
    Integer getCode();
    
    /**
     * 获取状态描述
     * @return 状态描述
     */
    String getMessage();
}