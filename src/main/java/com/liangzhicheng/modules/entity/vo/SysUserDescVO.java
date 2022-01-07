package com.liangzhicheng.modules.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 账号详情VO类
 * @author liangzhicheng
 */
@Data
@ApiModel(value="SysUserDescVO")
public class SysUserDescVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("账号id")
    private String id;
    @ApiModelProperty("公司id")
    private String companyId;
    @ApiModelProperty("公司名称")
    private String companyName;
    @ApiModelProperty("部门id")
    private String deptId;
    @ApiModelProperty("部门名称")
    private String deptName;
    @ApiModelProperty("角色id")
    private List<String> roleIds;
    @ApiModelProperty("角色名称")
    private List<String> roleNames;
    @ApiModelProperty("账号名称")
    private String accountName;
    @ApiModelProperty("真实姓名")
    private String truename;

}
