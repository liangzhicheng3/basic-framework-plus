package com.liangzhicheng.config.swagger.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Swagger属性类
 * @author liangzhicheng
 */
@Data
@ConfigurationProperties(prefix = "swagger")
@Component
public class SwaggerProperties {

    /**
     * 应用名
     */
    private String applicationName;

    /**
     * 版本信息
     */
    private String applicationVersion;

    /**
     * 描述信息
     */
    private String applicationDescription;

}
