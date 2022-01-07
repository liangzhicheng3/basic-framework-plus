package com.liangzhicheng.modules.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 地区VO类
 * @author liangzhicheng
 */
@Data
@ApiModel(value="AreaVO")
public class AreaVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("地区id")
    private String areaId;
    @ApiModelProperty("地区名称")
    private String areaName;

}
