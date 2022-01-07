package com.liangzhicheng.modules.entity.query;

import com.liangzhicheng.common.response.annotation.Query;
import com.liangzhicheng.common.utils.ToolUtil;
import com.liangzhicheng.modules.entity.dto.SysUserDTO;
import com.liangzhicheng.modules.entity.query.basic.BaseQueryCondition;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 用户查询条件封装类
 * @author liangzhicheng
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class SysUserQueryCondition extends BaseQueryCondition implements Serializable {

    private static final long serialVersionUID = 1L;

    @Query(type = Query.Type.INNER_LIKE, blurry = "id,truename")
    private String keyword;

    public SysUserQueryCondition(SysUserDTO userDTO){
        super(userDTO);
        String keyword = userDTO.getKeyword();
        if(ToolUtil.isNotBlank(keyword)) {
            this.keyword = keyword;
        }
    }

}
