package com.liangzhicheng.modules.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 账号登录VO类
 * @author liangzhicheng
 */
@Data
@ApiModel(value="SysUserLoginVO")
public class SysUserLoginVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("账号id")
    private String id;
    @ApiModelProperty("账号名称")
    private String accountName;
    @ApiModelProperty("头像")
    private String avatar;
    @ApiModelProperty("超级管理员：0否，1是")
    private String isAdmin;
    @ApiModelProperty("角色id")
    private String roleId;
    @ApiModelProperty("权限菜单列表")
    private List<SysMenuVO> permMenuList;
    @ApiModelProperty("登录token")
    private String token;

}
