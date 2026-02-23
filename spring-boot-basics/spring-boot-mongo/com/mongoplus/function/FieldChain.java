package com.mongoplus.function;

import com.mongoplus.support.SFunction;
import com.mongoplus.toolkit.FunctionUtil;

/**
 * 嵌套字段
 * <p>强制的类型控制,如{@link #of(SFunction)}传入User::getRole,则{@link #then(SFunction)}必须传入getRole响应对象内容</p>
 * @author anwen
 */
public class FieldChain<T> {

    private final FunctionUtil.FunctionBuilder builder;

    private FieldChain(SFunction<?,?> root) {
        builder = FunctionUtil.builderFunction();
        builder.add(root);
    }

    public static <T,R> FieldChain<R> of(SFunction<T,R> root) {
        return new FieldChain<>(root);
    }

    @SuppressWarnings("unchecked")
    public <R> FieldChain<R> then(SFunction<T,R> function) {
        builder.add(function);
        return (FieldChain<R>) this;
    }

    public FieldChain<T> then(String fieldName) {
        builder.add(fieldName);
        return this;
    }

    public FieldChain<T> then(Class<?> from) {
        builder.add(from);
        return this;
    }

    public String build() {
        return builder.build();
    }

    public String build(boolean isOption) {
        return builder.build(isOption);
    }

    public String buildOption() {
        return build(true);
    }

}
