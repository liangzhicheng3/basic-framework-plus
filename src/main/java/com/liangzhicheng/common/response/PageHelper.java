package com.liangzhicheng.common.response;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liangzhicheng.modules.entity.dto.basic.BaseDTO;

/**
 * 分页查询类
 * @author liangzhicheng
 */
public class PageHelper {

    private PageHelper() {}

    private static PageHelper instance;

    public static PageHelper getInstance() {
        if(instance == null){
            instance = new PageHelper();
        }
        return instance;
    }

    public Page handle(BaseDTO baseDTO) {
        Integer pageNum = baseDTO.getPageNum();
        Integer pageSize = baseDTO.getPageSize();
        if(pageNum == null || pageNum < 1){
            pageNum = 1;
        }
        if(pageSize == null || pageSize < 1){
            pageSize = 10;
        }
        return new Page(pageNum, pageSize);
    }

}
