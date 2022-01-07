package com.liangzhicheng.common.response.annotation;

import java.lang.annotation.*;

/**
 * 查询注解
 * @author liangzhicheng
 */
@Documented
@Inherited
@Target({ElementType.FIELD, ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Query {

    /**
     * 基本对象属性名
     */
    String propName() default "";

    /**
     * 查询方式
     */
    Type type() default Type.EQUAL;

    /**
     * 多字段模糊搜索，仅支持String类型字段，多个用逗号隔开，如@Query(blurry = "username,phone")
     */
    String blurry() default "";

    enum Type {

        EQUAL, //相等
        NOT_EQUAL, //不等于
        GREATER_THAN, //大于等于
        LESS_THAN, //小于等于
        LESS_THAN_NQ, //小于
        INNER_LIKE, //中模糊查询
        LEFT_LIKE, //左模糊查询
        RIGHT_LIKE, //右模糊查询
        IN, //包含
        BETWEEN, //区间
        NOT_NULL, //不为空
        UNIX_TIMESTAMP //时间查询

    }

}

