package com.liangzhicheng.modules.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 角色权限信息表
 * </p>
 *
 * @author liangzhicheng
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_role_perm")
public class SysRolePermEntity extends Model<SysRolePermEntity> {

    /**
     * 角色权限id(主键)
     */
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    /**
     * 角色id
     */
    private String roleId;

    /**
     * 权限id
     */
    private String permId;

    /**
     * 权限名称
     */
    private String permName;

    /**
     * 表达式
     */
    private String expression;

    /**
     * 删除标记-平台：0否，1是
     */
    private String delFlag;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createDate;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateDate;

    public SysRolePermEntity() {
        super();
    }

    public SysRolePermEntity(String id, String roleId, String permId, String permName, String expression) {
        this.id = id;
        this.roleId = roleId;
        this.permId = permId;
        this.permName = permName;
        this.expression = expression;
    }

}
