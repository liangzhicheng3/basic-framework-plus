package com.liangzhicheng.modules.service.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.liangzhicheng.common.utils.ToolUtil;
import com.liangzhicheng.modules.entity.query.basic.BaseQueryCondition;
import com.liangzhicheng.modules.service.IBaseService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 服务实现基类
 * @author liangzhicheng
 */
public abstract class BaseServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements IBaseService<T> {

    /**
     * 自定义排序
     * @param orders
     * @return Pageable
     */
    protected Pageable customizeSort(BaseQueryCondition query, List<Sort.Order> orders){
        return PageRequest.of(query.getPageNum(), query.getPageSize(), Sort.by(orders));
    }

    /**
     * 分页处理
     * @param pageNo
     * @param pageSize
     */
    protected void pageHandle(int pageNo, int pageSize){
        PageHelper.startPage(pageNo, pageSize);
    }

    /**
     * 自定义分页处理
     * @param pageable
     * @param pageNo
     * @param pageSize
     */
    protected void pageHandle(Pageable pageable, int pageNo, int pageSize){
        String sort = "";
        if(ToolUtil.isNotNull(pageable.getSort())){
            sort = pageable.getSort().toString();
            sort = sort.replace(":", "");
            if("UNSORTED".equals(sort)){
                sort = "create_date DESC";
            }
        }
        PageHelper.startPage(pageNo, pageSize, sort);
    }

    /**
     * 分页结果集
     * @param records
     * @param pageInfo
     * @param <T>
     * @return Map<String, Object>
     */
    protected <T> Map<String, Object> pageResult(List<T> records, PageInfo<T> pageInfo){
        Map<String, Object> resultMap = new LinkedHashMap<>(5);
        resultMap.put("total", pageInfo.getTotal());
        resultMap.put("pages", pageInfo.getPages());
        resultMap.put("pageNo", pageInfo.getPageNum());
        resultMap.put("pageSize", pageInfo.getPageSize());
        resultMap.put("records", records);
        return resultMap;
    }

}
