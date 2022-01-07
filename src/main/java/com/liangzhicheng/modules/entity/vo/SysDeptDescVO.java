package com.liangzhicheng.modules.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 部门详情VO类
 * @author liangzhicheng
 */
@Data
@ApiModel(value="SysDeptDescVO")
public class SysDeptDescVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("部门id")
    private String id;
    @ApiModelProperty("部门名称")
    private String name;
    @ApiModelProperty("公司id")
    private String companyId;
    @ApiModelProperty("公司名称")
    private String companyName;
    @ApiModelProperty("部门描述")
    private String description;

}
