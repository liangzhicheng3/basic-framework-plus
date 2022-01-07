package com.liangzhicheng.modules.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("area_name")
public class AreaNameEntity extends Model<AreaNameEntity> {

    /**
     * 语言
     */
    private String lang;

    /**
     * 地区名称
     */
    private String areaName;

    /**
     * 地区编码
     */
    private String areaCode;

    /**
     * 类型
     */
    private String type;

    /**
     * 是否编辑：0否，1是
     */
    private String isEdit;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 引用字段
     */
    @TableField(exist = false)
    private String areaId;
    @TableField(exist = false)
    private String areaLevel;
    @TableField(exist = false)
    private String country;
    @TableField(exist = false)
    private String province;
    @TableField(exist = false)
    private String city;

}
