package com.liangzhicheng.config.mvc.interceptor;

import com.liangzhicheng.common.bean.RedisBean;
import com.liangzhicheng.common.constant.ApiConstant;
import com.liangzhicheng.common.response.ResponseResult;
import com.liangzhicheng.common.utils.HttpUtil;
import com.liangzhicheng.common.utils.JSONUtil;
import com.liangzhicheng.common.utils.PrintUtil;
import com.liangzhicheng.common.utils.ToolUtil;
import com.liangzhicheng.config.context.SpringContextHolder;
import com.liangzhicheng.config.mvc.interceptor.annotation.AccessLimitValidate;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

/**
 * 访问限制拦截器，凡是方法头部加了注解@AccessLimitValidate的controller，执行前都会先执行下面的preHandle()方法
 * @author liangzhicheng
 */
public class AccessLimitInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if(handler.getClass().isAssignableFrom(HandlerMethod.class)){
			PrintUtil.info("[请求校验] 执行开始 >>>>>>");
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			AccessLimitValidate accessLimitValidate = handlerMethod.getMethodAnnotation(AccessLimitValidate.class);
			if(accessLimitValidate != null && accessLimitValidate.validate() == true){
				int second = accessLimitValidate.second(); //请求时间范围
				int times = accessLimitValidate.times(); //请求次数
				//根据IP + API限流
				String key = HttpUtil.getClientUrl(request) + request.getRequestURI();
				RedisBean redisBean = SpringContextHolder.getBean(RedisBean.class);
				//根据key获取已请求次数
				Integer maxTimes = Integer.parseInt(redisBean.get(key));
				if(maxTimes == null){
					redisBean.set(key, 1 + "", second, TimeUnit.SECONDS);
				}else if(maxTimes < times){
					redisBean.set(key, (maxTimes + 1) + "", second, TimeUnit.SECONDS);
				}else{
					render(response);
					return false;
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
			result = new ResponseResult(ApiConstant.REQUEST_BUSY, ApiConstant.getMessage(ApiConstant.REQUEST_BUSY), null);
			response.setContentType("application/json;charset=UTF-8");
			response.setCharacterEncoding("UTF-8");
			out = response.getWriter();
			out.println(JSONUtil.toJSONString(result));
		} catch (Exception e) {
			PrintUtil.error("[请求校验] JSON异常，异常信息为：{}", e.getMessage());
		}finally{
			if(ToolUtil.isNotNull(out)){
				out.flush();
				out.close();
			}
		}
	}

}
