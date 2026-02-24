package com.spring.elastic.common.enums.business;

import java.util.Arrays;
import java.util.Optional;

/**
 * 状态类枚举通用接口
 *
 * @author qujianlei
 * @since 1.0.0
 */
public interface BaseEnum {
    
    /**
     * 获取枚举编码
     * 
     * @return 编码
     */
    Integer getCode();

    /**
     * 获取枚举描述
     * 
     * @return 描述
     */
    String getDesc();

    /**
     * 根据 code 获取枚举对象
     *
     * @param enumClass 枚举类
     * @param code      编码
     * @param <T>       枚举类型
     * @return 对应的枚举对象，如果找不到则返回 null
     */
    static <T extends Enum<?> & BaseEnum> T getByCode(Class<T> enumClass, Integer code) {
        if (code == null) {
            return null;
        }
        // 使用 Java 8 Stream 查找
        return Arrays.stream(enumClass.getEnumConstants())
                .filter(e -> e.getCode().equals(code))
                .findFirst()
                .orElse(null);
    }

    /**
     * 根据 code 获取枚举对象，提供默认值
     *
     * @param enumClass    枚举类
     * @param code         编码
     * @param defaultValue 默认值
     * @param <T>          枚举类型
     * @return 对应的枚举对象
     */
    static <T extends Enum<?> & BaseEnum> T getByCode(Class<T> enumClass, Integer code, T defaultValue) {
        return Optional.ofNullable(getByCode(enumClass, code)).orElse(defaultValue);
    }

    
    /**
     * 判断枚举编码是否合法
     *
     * @param enumClass 枚举类
     * @param code      编码
     * @param <T>       枚举类型
     * @return true: 合法, false: 不合法
     */
    static <T extends Enum<T> & BaseEnum> boolean isValidCode(Class<T> enumClass, Integer code) {
        if (code == null) {
            return false;
        }

        // 使用 Java 8 Stream 判断是否存在
        return Arrays.stream(enumClass.getEnumConstants())
                .anyMatch(e -> e.getCode().equals(code));
    }
}
