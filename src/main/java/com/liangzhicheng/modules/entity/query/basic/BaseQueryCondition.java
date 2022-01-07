package com.liangzhicheng.modules.entity.query.basic;

import com.liangzhicheng.modules.entity.dto.basic.BaseDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 基础查询条件封装类
 * @author liangzhicheng
 */
@Data
@NoArgsConstructor
public class BaseQueryCondition implements Serializable {

    private static final long serialVersionUID = 1L;

    private int pageNum;
    private int pageSize;

    public BaseQueryCondition(BaseDTO baseDTO){
        Integer pageNum = baseDTO.getPageNum();
        Integer pageSize = baseDTO.getPageSize();
        if(pageNum == null || pageNum < 1){
            pageNum = 1;
        }
        if(pageSize == null || pageSize < 1){
            pageSize = 10;
        }
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    public int getPageNo(){
        return (this.pageNum - 1) * pageSize;
    }

}
