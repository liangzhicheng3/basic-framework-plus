package com.liangzhicheng.modules.entity.dto.basic;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 基础数据传输对象
 * @author liangzhicheng
 */
@Data
public class BaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "页码不能为空")
    @ApiModelProperty(value = "页码")
    private Integer pageNum;
    @NotNull(message = "每页数量不能为空")
    @ApiModelProperty(value = "每页数量")
    private Integer pageSize;

}
