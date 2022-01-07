package com.liangzhicheng.config.mvc.interceptor;

import com.liangzhicheng.common.constant.ApiConstant;
import com.liangzhicheng.common.response.ResponseResult;
import com.liangzhicheng.common.utils.HttpUtil;
import com.liangzhicheng.common.utils.JSONUtil;
import com.liangzhicheng.common.utils.PrintUtil;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;
import java.util.Map;

/**
 * XSS攻击拦截器
 * @author liangzhicheng
 */
public class XSSInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		PrintUtil.info("[XSS校验] 执行开始 >>>>>>");
		Map<String, String[]> requestParams = request.getParameterMap();
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
				String newStr = HttpUtil.replaceXSS(valueStr);
				if(!valueStr.equals(newStr)) {
					PrintUtil.info("[XSS校验] 参数->valueStr : " + valueStr);
					PrintUtil.info("[XSS校验] 参数->newStr : " + newStr);
					response.setCharacterEncoding("UTF-8");
					response.setContentType("application/json");
					ResponseResult result = new ResponseResult(ApiConstant.BASE_FAIL_CODE, "含有XSS字符", null);
					response.getWriter().print(JSONUtil.toJSONString(result));
					return false;
				}
			}
		}
		return true;
	}

}
