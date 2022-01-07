package com.liangzhicheng.modules.service;

import com.liangzhicheng.modules.entity.AreaNameEntity;
import com.liangzhicheng.modules.entity.dto.AreaDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 地区名称信息表 服务类
 * </p>
 *
 * @author liangzhicheng
 */
public interface IAreaNameService extends IBaseService<AreaNameEntity> {

    /**
     * 地区列表
     * @param areaDTO
     * @param pageable
     * @return Map<String, Object>
     */
    Map<String, Object> listArea(AreaDTO areaDTO, Pageable pageable);

    /**
     * 查询地区信息
     * @param areaName
     * @return List<Map<String, Object>>
     */
    List<Map<String, Object>> getArea(AreaNameEntity areaName);

}
