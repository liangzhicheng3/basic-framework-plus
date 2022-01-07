package com.liangzhicheng.config.mvc.resolver.annotation;

import java.lang.annotation.*;

/**
 * 用户参数注解
 * @author liangzhicheng
 */
@Documented
@Inherited
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface UserParam {

}
