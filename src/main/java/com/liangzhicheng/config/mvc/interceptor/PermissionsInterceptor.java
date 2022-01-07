package com.liangzhicheng.config.mvc.interceptor;

import com.liangzhicheng.common.bean.RedisBean;
import com.liangzhicheng.common.constant.ApiConstant;
import com.liangzhicheng.common.response.ResponseResult;
import com.liangzhicheng.common.utils.JSONUtil;
import com.liangzhicheng.common.utils.PrintUtil;
import com.liangzhicheng.common.utils.ToolUtil;
import com.liangzhicheng.config.context.SpringContextHolder;
import com.liangzhicheng.config.mvc.interceptor.annotation.PermissionsValidate;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Set;

/**
 * 权限校验拦截器，凡是方法头部加了注解@PermissionsValidate的controller，执行前都会先执行下面的preHandle()方法
 * @author liangzhicheng
 */
public class PermissionsInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(handler.getClass().isAssignableFrom(HandlerMethod.class)){
            PrintUtil.info("[权限校验] 执行开始 >>>>>>");
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            PermissionsValidate permissionsValidate = handlerMethod.getMethodAnnotation(PermissionsValidate.class);
            if(permissionsValidate != null && permissionsValidate.validate() == true){
                String expression = permissionsValidate.expression();
                String accountId = request.getHeader("accountId");
                PrintUtil.info("[权限校验] 参数->expression : " + expression);
                PrintUtil.info("[权限校验] 参数->accountId : " + accountId);
                if(ToolUtil.isNotBlank(expression, accountId)){
                    RedisBean redisBean = SpringContextHolder.getBean(RedisBean.class);
                    Map<String, Object> permMap = redisBean.getPermMap();
                    Set<String> perms = (Set<String>) permMap.get(accountId);
                    if(ToolUtil.isNotNull(perms)){
                        Integer totalPermNum = perms.size();
                        Integer permNum = totalPermNum;
                        for(String permExpression : perms){
                            if(expression.equals(permExpression)){
                                permNum -= 1;
                            }
                        }
                        if(totalPermNum == permNum){
                            render(response);
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * 异常返回参数输出
     * @param response
     */
    private void render(HttpServletResponse response) {
        PrintWriter out = null;
        ResponseResult result = null;
        try {
            result = new ResponseResult(ApiConstant.NO_AUTHORIZATION, ApiConstant.getMessage(ApiConstant.NO_AUTHORIZATION), null);
            response.setContentType("application/json;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(JSONUtil.toJSONString(result));
        } catch (Exception e) {
            PrintUtil.error("[权限校验] JSON异常，异常信息为：{}", e.getMessage());
        }finally{
            if(out != null){
                out.flush();
                out.close();
            }
        }
    }

}
