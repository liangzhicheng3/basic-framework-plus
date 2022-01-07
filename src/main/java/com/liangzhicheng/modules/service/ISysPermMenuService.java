package com.liangzhicheng.modules.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liangzhicheng.modules.entity.SysMenuEntity;
import com.liangzhicheng.modules.entity.SysPermEntity;
import com.liangzhicheng.modules.entity.SysPermMenuEntity;

/**
 * <p>
 * 权限菜单信息表 服务类
 * </p>
 *
 * @author liangzhicheng
 */
public interface ISysPermMenuService extends IService<SysPermMenuEntity> {

    /**
     * 根据key，value获取权限菜单
     * @param key
     * @param value
     * @return SysPermMenuEntity
     */
    SysPermMenuEntity getOne(String key, String value);

    /**
     * 新增权限菜单
     * @param perm
     * @param menu
     */
    void insertPermMenu(SysPermEntity perm, SysMenuEntity menu);

}
