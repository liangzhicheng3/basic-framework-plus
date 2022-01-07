package com.liangzhicheng.modules.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("area_code")
public class AreaCodeEntity extends Model<AreaCodeEntity> {

    /**
     * 地区id
     */
    @TableId(value = "area_id", type = IdType.INPUT)
    private String areaId;

    /**
     * 地区编码
     */
    private String areaCode;

    /**
     * 地区层级
     */
    private Integer areaLevel;

}
