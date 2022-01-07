package com.liangzhicheng.config.mvc.interceptor;

import com.liangzhicheng.common.constant.ApiConstant;
import com.liangzhicheng.common.response.ResponseResult;
import com.liangzhicheng.common.utils.JSONUtil;
import com.liangzhicheng.common.utils.JWTUtil;
import com.liangzhicheng.common.utils.PrintUtil;
import com.liangzhicheng.common.utils.ToolUtil;
import com.liangzhicheng.config.mvc.interceptor.annotation.LoginValidate;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * 登录校验拦截器，凡是方法头部加了注解@LoginValidate的controller，执行前都会先执行下面的preHandle()方法
 * @author liangzhicheng
 */
public class LoginInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if(handler.getClass().isAssignableFrom(HandlerMethod.class)){
			PrintUtil.info("[登录校验] 执行开始 >>>>>>");
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			LoginValidate loginValidate = handlerMethod.getMethodAnnotation(LoginValidate.class);
			if(loginValidate != null && loginValidate.validate() == true){
				boolean isLogin = false;
				String tokenMINI = request.getHeader("tokenMINI");
				String userId = request.getHeader("userId");
				PrintUtil.info("[登录校验] 参数->tokenMINI：{}", tokenMINI);
				PrintUtil.info("[登录校验] 参数->userId：{}", userId);
				if(ToolUtil.isNotBlank(tokenMINI, userId)){
					isLogin = JWTUtil.isLoginMINI(userId, tokenMINI);
				}
				/*
				 * 如果没有登录，返回false拦截请求
				 */
				if(!isLogin){
					ResponseResult result = null;
					PrintWriter out = null;
					try{
						result = new ResponseResult(ApiConstant.NO_LOGIN, ApiConstant.getMessage(ApiConstant.NO_LOGIN), null);
						response.setContentType("application/json;charset=UTF-8");
						response.setCharacterEncoding("UTF-8");
						out = response.getWriter();
						out.println(JSONUtil.toJSONString(result));
					}catch(Exception e){
						PrintUtil.error("[登录校验] JSON异常，异常信息为：{}", e.getMessage());
					}finally{
						if(ToolUtil.isNotNull(out)){
							out.flush();
							out.close();
						}
					}
					return false;
				}
			}
		}
		return true;
	}

}
