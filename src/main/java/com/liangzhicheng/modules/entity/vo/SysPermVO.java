package com.liangzhicheng.modules.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 权限信息表
 * </p>
 *
 * @author liangzhicheng
 */
@Data
@ApiModel(value="SysPermVO")
public class SysPermVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("权限id")
    private String id;
    @ApiModelProperty("权限名称")
    private String name;
    @ApiModelProperty("表达式")
    private String expression;

}
