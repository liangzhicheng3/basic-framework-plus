package com.liangzhicheng.modules.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 菜单相关数据传输对象
 * @author liangzhicheng
 */
@Data
public class SysMenuDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 保存参数
     */
    @ApiModelProperty("菜单id")
    private String id;
    @ApiModelProperty("父级id")
    private String parentId;
    @ApiModelProperty("菜单名称")
    private String name;
    @ApiModelProperty("父组件")
    private String component;
    @ApiModelProperty("路由路径")
    private String routerPath;
    @ApiModelProperty("重定向")
    private String redirect;
    @ApiModelProperty("是否隐藏：0显示，1隐藏")
    private String isHide;
    @ApiModelProperty("排序")
    private Integer rank;


    @ApiModelProperty("权限id(主键)")
    private String permId;
    @ApiModelProperty("权限名称")
    private String permName;
    @ApiModelProperty("表达式")
    private String expression;

}
