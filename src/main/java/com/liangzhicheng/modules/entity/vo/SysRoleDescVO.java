package com.liangzhicheng.modules.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 角色详情VO类
 * @author liangzhicheng
 */
@Data
@ApiModel(value="SysRoleDescVO")
public class SysRoleDescVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("角色id")
    private String id;
    @ApiModelProperty("角色名称")
    private String name;
    @ApiModelProperty("职务描述")
    private String description;
    @ApiModelProperty("菜单id列表")
    private List<String> menuIds;
    @ApiModelProperty("权限id列表")
    private List<String> permIds;

}
