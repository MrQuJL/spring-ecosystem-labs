package com.spring.minimal.common.enums.business;

import com.spring.minimal.common.utils.EnumUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 逻辑删除状态枚举
 * <p>用于标识数据的逻辑删除状态</p>
 *
 * @author qujianlei
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public enum DeletedEnum implements BaseEnum {

    NOT_DELETED(0, "未删除"),
    DELETED(1, "已删除");

    private final Integer code;
    private final String desc;

    /**
     * 根据状态码获取枚举
     * <p>严厉模式：如果 code 不存在，返回 null，由调用方处理异常</p>
     *
     * @param code 状态码
     * @return DeletedEnum 或 null
     */
    public static DeletedEnum getByCode(Integer code) {
        return EnumUtil.getByCode(DeletedEnum.class, code);
    }
}
