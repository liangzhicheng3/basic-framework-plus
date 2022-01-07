package com.liangzhicheng.config.mvc.resolver;

import com.liangzhicheng.common.utils.ToolUtil;
import com.liangzhicheng.config.mvc.resolver.annotation.UserParam;
import com.liangzhicheng.modules.entity.SysUserEntity;
import com.liangzhicheng.modules.service.ISysUserService;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 用户参数解析器
 * @author liangzhicheng
 */
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

    @Resource
    private ISysUserService userService;

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(UserParam.class)
                && methodParameter.getParameterType().equals(SysUserEntity.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter,
                                  ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest,
                                  WebDataBinderFactory webDataBinderFactory) throws Exception {
        HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        if(request == null){
            return null;
        }
        String userId = request.getHeader("userId");
        if(ToolUtil.isNotBlank(userId)){
            return userService.getByCache(userId);
        }
        return null;
    }

}
