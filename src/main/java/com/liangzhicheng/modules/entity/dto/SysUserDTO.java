package com.liangzhicheng.modules.entity.dto;

import com.liangzhicheng.modules.entity.dto.basic.BaseDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 账号相关数据传输对象
 * @author liangzhicheng
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysUserDTO extends BaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 公用参数
     */
    @ApiModelProperty("部门id")
    private String deptId;
    @ApiModelProperty("角色id")
    private String roleIds;

    /**
     * 查询参数
     */
    @ApiModelProperty("账号id/姓名")
    private String keyword;

    /**
     * 保存参数
     */
    @ApiModelProperty("账号id(主键)")
    private String id;
    @ApiModelProperty("账号名称")
    private String accountName;
    @ApiModelProperty("真实姓名")
    private String truename;
    @ApiModelProperty("密码")
    private String password;
    @ApiModelProperty("头像")
    private String avatar;
    @ApiModelProperty("超级管理员：0否，1是")
    private String isAdmin;
    @ApiModelProperty("登录状态：0冻结，1正常")
    private String loginStatus;


    @ApiModelProperty("新密码")
    private String newPassword;

}
