package com.liangzhicheng.config.mvc.interceptor.annotation;

import java.lang.annotation.*;

/**
 * 权限校验注解
 * @author liangzhicheng
 */
@Documented
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PermissionsValidate {

    boolean validate() default true;

    String expression(); //表达式

}
