package com.liangzhicheng.modules.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 部门信息表
 * </p>
 *
 * @author liangzhicheng
 */
@Data
@ApiModel(value="SysDeptVO")
public class SysDeptVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("部门id(主键)")
    private String id;
    @ApiModelProperty("部门名称")
    private String name;
    @ApiModelProperty("部门描述")
    private String description;
    @ApiModelProperty("排序")
    private Integer rank;

}
