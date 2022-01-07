package com.liangzhicheng.modules.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 【微信】登录相关数据传输对象
 * @author liangzhicheng
 */
@Data
public class LoginWeChatDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "code不能为空")
    @ApiModelProperty(value = "微信code")
    private String code;
    @ApiModelProperty(value = "用户信息密文")
    private String encryptedData;
    @ApiModelProperty(value = "解密算法初始向量")
    private String iv;

}
