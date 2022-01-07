package com.liangzhicheng.modules.entity.dto;

import com.liangzhicheng.modules.entity.dto.basic.BaseDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
public class AreaDTO extends BaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "地区id") //areaLevel为0时可以为空
    private String areaId;
    @NotBlank(message = "地区层级不能为空")
    @ApiModelProperty(value = "地区层级：0国，1省，2市，3区")
    private String areaLevel;

}
