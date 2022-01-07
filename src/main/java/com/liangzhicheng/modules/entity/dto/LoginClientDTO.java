package com.liangzhicheng.modules.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 登录相关数据传输对象
 * @author liangzhicheng
 */
@Data
public class LoginClientDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户id")
    private String userId;
    @ApiModelProperty(value = "手机号码")
    private String phone;
    @ApiModelProperty(value = "短信验证码")
    private String vcode;

}
