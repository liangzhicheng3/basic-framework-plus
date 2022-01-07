package com.liangzhicheng.modules.entity.dto;

import com.liangzhicheng.modules.entity.dto.basic.BaseDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 部门相关数据传输对象
 * @author liangzhicheng
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysDeptDTO extends BaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 查询参数
     */
    @ApiModelProperty(value = "部门名称")
    private String keyword;
    @ApiModelProperty(value = "创建时间")
    private String createDate;

    /**
     * 保存参数
     */
    @ApiModelProperty("部门id")
    private String id;
    @ApiModelProperty("部门名称")
    private String name;
    @ApiModelProperty("部门描述")
    private String description;

}
