package com.liangzhicheng.config.aop;

import com.liangzhicheng.common.basic.BaseController;
import com.liangzhicheng.common.constant.ApiConstant;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

/**
 * 针对Controller层RequestBody的Spring Validator校验统一返回格式
 * @author liangzhicheng
 */
@Aspect
@Component
public class RequestBodyValidateAspect extends BaseController {

    /**
     * 统一错误返回信息切入点
     */
    @Pointcut("execution(* com.liangzhicheng.modules..*.*Controller.*(..))")
    public void validateCut(){}

    /**
     * 统一错误返回信息为参数名加错误信息
     * @param joinPoint
     * @return Object
     */
    @Around("validateCut()")
    public Object validateAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof BindingResult) {
                BindingResult result = (BindingResult) arg;
                if (result.hasErrors()) {
                    FieldError fieldError = result.getFieldError();
                    if(fieldError != null){
                        return buildFailedInfo(fieldError.getDefaultMessage());
                    }else{
                        return buildFailedInfo(ApiConstant.BASE_FAIL_CODE);
                    }
                }
            }
        }
        return joinPoint.proceed();
    }

}
