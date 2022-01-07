package com.liangzhicheng.modules.entity.dto;

import com.liangzhicheng.config.mvc.validate.annotation.FlagValidate;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class TestDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "名称不能为空")
    private String name;
    @NotNull(message = "年龄不能为空")
    @Min(value = 0, message = "年龄最小为0")
    private Integer age;
    @FlagValidate(value = {"1", "2"}, message = "状态不正确")
    private Integer status;

}
