package com.liangzhicheng.modules.entity.query;

import com.liangzhicheng.common.utils.ToolUtil;
import com.liangzhicheng.modules.entity.dto.AreaDTO;
import com.liangzhicheng.modules.entity.query.basic.BaseQueryCondition;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 地区查询条件封装类
 * @author liangzhicheng
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AreaQueryCondition extends BaseQueryCondition implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 地区id
     */
    private String areaId;

    /**
     * 地区层级
     */
    private String areaLevel;

    /**
     * 长度
     */
    private String length;

    public AreaQueryCondition(AreaDTO areaDTO) {
        super(areaDTO);
        String areaId = areaDTO.getAreaId();
        if(ToolUtil.isNotBlank(areaId)) {
            this.areaId = areaId;
            this.length = String.valueOf(areaId.length());
        }
        String areaLevel = areaDTO.getAreaLevel();
        if(ToolUtil.isNotBlank(areaLevel)) {
            this.areaLevel = areaLevel;
        }
    }

}
