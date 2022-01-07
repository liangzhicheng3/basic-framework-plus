package com.liangzhicheng.modules.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 角色权限信息表
 * </p>
 *
 * @author liangzhicheng
 */
@Data
@ApiModel(value="SysRolePermVO")
public class SysRolePermVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("角色id")
    private String roleId;
    @ApiModelProperty("权限id")
    private String permId;
    @ApiModelProperty("权限名称")
    private String permName;
    @ApiModelProperty("表达式")
    private String expression;

}
