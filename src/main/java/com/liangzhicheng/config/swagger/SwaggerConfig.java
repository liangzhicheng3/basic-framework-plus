package com.liangzhicheng.config.swagger;

import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import com.liangzhicheng.config.swagger.properties.SwaggerProperties;
import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * swagger接口文档配置
 * @author liangzhicheng
 */
@EnableSwagger2
@EnableSwaggerBootstrapUI
@Configuration
public class SwaggerConfig {

    @Resource
    private Environment env;
    @Resource
    private SwaggerProperties properties;

    @Bean
    public Docket docketTest(){
        return docket(newHashMap("/test/**", "测试管理"));
    }

    @Bean
    public Docket docketClientLogin(){
        return docket(newHashMap("/client/**", "登录管理-客户端"));
    }

    @Bean
    public Docket docketArea(){
        return docket(newHashMap("/area/**", "地区管理"));
    }

    @Bean
    public Docket docketPay(){
        return docket(newHashMap("/pay/**", "支付管理"));
    }

    @Bean
    public Docket docketUpload(){
        return docket(newHashMap("/file/**", "文件管理"));
    }

    @Bean
    public Docket docketServerLogin(){
        return docket(newHashMap("/server/**", "登录管理-服务端"));
    }

    @Bean
    public Docket docketAccount(){
        return docket(newHashMap("/account/**", "账号管理"));
    }

    @Bean
    public Docket docketDept(){
        return docket(newHashMap("/dept/**", "部门管理"));
    }

    @Bean
    public Docket docketMenu(){
        return docket(newHashMap("/menu/**", "菜单管理"));
    }

    @Bean
    public Docket docketRole(){
        return docket(newHashMap("/role/**", "角色管理"));
    }

    @Bean
    public Docket docketConfig(){
        return docket(newHashMap("/config/**", "配置管理"));
    }

    public Docket docket(Map<String, String> map) {
        //设置Api接口文档在环境中显示
        Profiles profiles = Profiles.of("dev", "beta");
        //判断当前是否处于该环境
        boolean isEnable = env.acceptsProfiles(profiles);
        return new Docket(DocumentationType.SWAGGER_2)
                .enable(isEnable)
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.ant(map.get("path")))
                .build()
                .apiInfo(apiInfo())
                .groupName(map.get("groupName"));
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(properties.getApplicationName())
                .description(properties.getApplicationDescription())
                .contact(new Contact("liangzhicheng", "https://github.com/liangzhicheng3", "yichengc3@163.com"))
                .version(properties.getApplicationVersion())
                .build();
    }

    private Map<String, String> newHashMap(String path, String groupName){
        Map<String, String> map = new HashMap<>(2);
        map.put("path", path);
        map.put("groupName", groupName);
        return map;
    }

}
