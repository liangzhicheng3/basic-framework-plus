package com.liangzhicheng.modules.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 权限菜单信息表
 * </p>
 *
 * @author liangzhicheng
 */
@Data
@ApiModel(value="SysPermMenuVO")
public class SysPermMenuVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("权限菜单id(主键)")
    private String id;
    @ApiModelProperty("权限id")
    private String permId;
    @ApiModelProperty("权限名称")
    private String permName;
    @ApiModelProperty("菜单id")
    private String menuId;
    @ApiModelProperty("菜单名称")
    private String menuName;

}
