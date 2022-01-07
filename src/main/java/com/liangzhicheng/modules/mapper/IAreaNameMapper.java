package com.liangzhicheng.modules.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.liangzhicheng.modules.entity.AreaNameEntity;
import com.liangzhicheng.modules.entity.query.AreaQueryCondition;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 地区名称信息表 Mapper 接口
 * </p>
 *
 * @author liangzhicheng
 */
public interface IAreaNameMapper extends BaseMapper<AreaNameEntity> {

    List<Map<String, Object>> listArea(AreaQueryCondition areaQuery);

    List<Map<String, Object>> getArea(AreaNameEntity areaName);

}
