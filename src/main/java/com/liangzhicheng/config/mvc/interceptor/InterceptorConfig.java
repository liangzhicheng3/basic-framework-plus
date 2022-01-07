package com.liangzhicheng.config.mvc.interceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 拦截器相关配置
 * @author liangzhicheng
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Bean
    XSSInterceptor xssInterceptor(){
        return new XSSInterceptor();
    }

    @Bean
    AccessLimitInterceptor accessLimitInterceptor(){
        return new AccessLimitInterceptor();
    }

    @Bean
    LoginInterceptor loginInterceptor(){
        return new LoginInterceptor();
    }

    @Bean
    PermissionsInterceptor permissionsInterceptor(){
        return new PermissionsInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(xssInterceptor()).addPathPatterns("/**");
        registry.addInterceptor(accessLimitInterceptor()).addPathPatterns("/**");
        registry.addInterceptor(loginInterceptor()).addPathPatterns("/**");
        registry.addInterceptor(permissionsInterceptor()).addPathPatterns("/**");
    }

}
