package com.liangzhicheng.modules.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 个人信息VO类
 * @author liangzhicheng
 */
@Data
@ApiModel(value="SysPersonInfoVO")
public class SysPersonInfoVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("头像")
    private String avatar;

}
