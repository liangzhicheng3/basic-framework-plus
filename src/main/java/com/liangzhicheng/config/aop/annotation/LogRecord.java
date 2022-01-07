package com.liangzhicheng.config.aop.annotation;

import java.lang.annotation.*;

/**
 * 日志记录注解
 * @author liangzhicheng
 */
@Documented
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogRecord {

    String operate() default "操作日志"; //操作记录值

}
