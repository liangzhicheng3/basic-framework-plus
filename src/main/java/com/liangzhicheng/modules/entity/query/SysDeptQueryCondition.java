package com.liangzhicheng.modules.entity.query;

import com.liangzhicheng.common.response.annotation.Query;
import com.liangzhicheng.common.utils.ToolUtil;
import com.liangzhicheng.modules.entity.dto.SysDeptDTO;
import com.liangzhicheng.modules.entity.query.basic.BaseQueryCondition;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 部门查询条件封装类
 * @author liangzhicheng
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class SysDeptQueryCondition extends BaseQueryCondition implements Serializable {

    private static final long serialVersionUID = 1L;

    @Query(type = Query.Type.INNER_LIKE)
    private String name;
    @Query(type = Query.Type.BETWEEN)
    private String createDate;

    public SysDeptQueryCondition(SysDeptDTO deptDTO) {
        super(deptDTO);
        String keyword = deptDTO.getKeyword();
        if(ToolUtil.isNotBlank(keyword)) {
            this.name = keyword;
        }
        String createDate = deptDTO.getCreateDate();
        if(ToolUtil.isNotBlank(createDate)){
            this.createDate = createDate;
        }
    }

}
