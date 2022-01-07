package com.liangzhicheng.config.aop;

import com.liangzhicheng.common.utils.JSONUtil;
import com.liangzhicheng.common.utils.PrintUtil;
import com.liangzhicheng.common.utils.ToolUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * 监控Controller请求，对贴有@RequestParam为true的参数进行值校验
 * @author liangzhicheng
 */
@Aspect
@Component
public class RequestControllerAspect {

    /**
     * Controller层AOP监控处理切入点
     */
    @Pointcut("execution(* com.liangzhicheng.modules..*.*Controller.*(..))")
    public void controllerCut(){}

    /**
     * Controller层AOP监控处理，包括请求路径，入参参数，返回参数，响应时间
     * @param joinPoint
     * @return Object
     * @throws Throwable
     */
	@Around("controllerCut()")
    public Object controllerAround(ProceedingJoinPoint joinPoint) throws Throwable{
        //获取当前请求属性集
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        //获取请求
        HttpServletRequest request = requestAttributes.getRequest();
        //获取请求地址
        String requestUrl = request.getRequestURL().toString();
        //记录请求地址
        PrintUtil.info("请求路径[{}]", requestUrl);
        //记录请求开始时间
        long startTime = System.currentTimeMillis();
        try {
            Class<?> target = joinPoint.getTarget().getClass();
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            Class<?>[] paramTypes = methodSignature.getParameterTypes();
            String methodName = joinPoint.getSignature().getName();
            //获取当前执行方法
            Method currentMethod = target.getMethod(methodName, paramTypes);
            if(currentMethod != null){
                //拼接输出字符串
                String executing = String.format("%s的%s接口", target.getName(), currentMethod.getName());
                PrintUtil.info("正在执行：{}", executing);
                Object[] args = joinPoint.getArgs();
                if(ToolUtil.isNotNull(args)){
                    PrintUtil.info("请求参数：{}", JSONUtil.toJSONString(args[0]));
                }
            }
        } catch (Exception e) {
            PrintUtil.error("[控制器环绕] 发生异常:", e);
        }
        Object object = joinPoint.proceed();
        PrintUtil.info("结果返回: {}", object == null ? "空" : JSONUtil.toJSONString(object));
        long endTime = System.currentTimeMillis();
        PrintUtil.info("响应时间 {} 毫秒", endTime - startTime);
        return object;
    }

}
