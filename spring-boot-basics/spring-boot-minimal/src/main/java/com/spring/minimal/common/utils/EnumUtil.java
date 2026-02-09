package com.spring.minimal.common.utils;

import com.spring.minimal.common.enums.business.BaseEnum;

import java.util.Arrays;
import java.util.Optional;

/**
 * 枚举工具类
 *
 * @author qujianlei
 * @since 1.0.0
 */
public class EnumUtil {

    /**
     * 根据 code 获取枚举对象
     *
     * @param enumClass 枚举类
     * @param code      编码
     * @param <T>       枚举类型
     * @return 对应的枚举对象，如果找不到则返回 null
     */
    public static <T extends Enum<?> & BaseEnum> T getByCode(Class<T> enumClass, Integer code) {
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
    public static <T extends Enum<?> & BaseEnum> T getByCode(Class<T> enumClass, Integer code, T defaultValue) {
        return Optional.ofNullable(getByCode(enumClass, code)).orElse(defaultValue);
    }
}
