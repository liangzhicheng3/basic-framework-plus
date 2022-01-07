package com.liangzhicheng.config.mvc.resolver;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * 用户参数解析器注册
 * @author liangzhicheng
 */
@Configuration
public class UserParamConfig implements WebMvcConfigurer {

    @Bean
    public UserArgumentResolver userArgumentResolver(){
        return new UserArgumentResolver();
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(userArgumentResolver());
    }

}
