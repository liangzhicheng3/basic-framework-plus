package com.liangzhicheng.modules.service.impl;

import com.github.pagehelper.PageInfo;
import com.liangzhicheng.common.utils.BeansUtil;
import com.liangzhicheng.common.utils.ListUtil;
import com.liangzhicheng.modules.entity.AreaNameEntity;
import com.liangzhicheng.modules.entity.dto.AreaDTO;
import com.liangzhicheng.modules.entity.query.AreaQueryCondition;
import com.liangzhicheng.modules.entity.vo.AreaVO;
import com.liangzhicheng.modules.mapper.IAreaNameMapper;
import com.liangzhicheng.modules.service.IAreaNameService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 地区名称信息表 服务实现类
 * </p>
 *
 * @author liangzhicheng
 */
@Service
public class AreaNameServiceImpl extends BaseServiceImpl<IAreaNameMapper, AreaNameEntity> implements IAreaNameService {

    /**
     * 地区列表
     * @param areaDTO
     * @param pageable
     * @return Map<String, Object>
     */
    @Override
    public Map<String, Object> listArea(AreaDTO areaDTO, Pageable pageable) {
        AreaQueryCondition areaQuery = new AreaQueryCondition(areaDTO);
        super.pageHandle(areaQuery.getPageNum(), areaQuery.getPageSize());
        List<Map<String, Object>> areaList = baseMapper.listArea(areaQuery);
        PageInfo pageInfo = new PageInfo<>();
        List<?> records = null;
        if(ListUtil.sizeGT(areaList)){
            pageInfo = new PageInfo<>(areaList);
            records = BeansUtil.copyList(pageInfo.getList(), AreaVO.class);
        }
        return super.pageResult(records, pageInfo);
    }

    /**
     * 查询地区信息
     * @param areaName
     * @return List<Map<String, Object>>
     */
    @Override
    public List<Map<String, Object>> getArea(AreaNameEntity areaName) {
        return baseMapper.getArea(areaName);
    }

}
