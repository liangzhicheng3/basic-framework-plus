package com.liangzhicheng.common.response;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页相关封装类
 * @author liangzhicheng
 */
@Data
public class PageResult implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 当前页码
     */
    private Integer pageNo;

    /**
     * 每页数量
     */
    private Integer pageSize;

    /**
     * 总记录数
     */
    private Integer total;

    /**
     * 总页数
     */
    private Integer pages;

    /**
     * 结果集
     */
    private List<?> records;

    public PageResult(Integer pageNo, Integer pageSize, List<?> records, Integer total) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.records = records;
        this.total = total;
        if(total <= pageSize){
            this.pages = 1;
            return;
        }
        this.pages = total % pageSize == 0 ? total / pageSize : total / pageSize + 1;
    }

}
