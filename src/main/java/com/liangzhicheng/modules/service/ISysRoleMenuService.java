package com.liangzhicheng.modules.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liangzhicheng.modules.entity.SysRoleMenuEntity;

import java.util.List;

/**
 * <p>
 * 角色菜单信息表 服务类
 * </p>
 *
 * @author liangzhicheng
 */
public interface ISysRoleMenuService extends IService<SysRoleMenuEntity> {

    /**
     * 根据key，value获取角色菜单列表
     * @param key
     * @param value
     * @return List<SysRoleMenuEntity>
     */
    List<SysRoleMenuEntity> list(String key, String value);

}
