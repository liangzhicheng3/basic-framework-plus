package com.liangzhicheng.modules.entity.query;

import com.liangzhicheng.common.response.annotation.Query;
import com.liangzhicheng.common.utils.ToolUtil;
import com.liangzhicheng.modules.entity.dto.SysRoleDTO;
import com.liangzhicheng.modules.entity.query.basic.BaseQueryCondition;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 角色查询条件封装类
 * @author liangzhicheng
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class SysRoleQueryCondition extends BaseQueryCondition implements Serializable {

    private static final long serialVersionUID = 1L;

    @Query(type = Query.Type.INNER_LIKE, blurry = "id,name")
    private String keyword;
    @Query(type = Query.Type.BETWEEN)
    private String createDate;

    public SysRoleQueryCondition(SysRoleDTO roleDTO) {
        super(roleDTO);
        String keyword = roleDTO.getKeyword();
        if(ToolUtil.isNotBlank(keyword)) {
            this.keyword = keyword;
        }
        String createDate = roleDTO.getCreateDate();
        if(ToolUtil.isNotBlank(createDate)){
            this.createDate = createDate;
        }
    }

}
