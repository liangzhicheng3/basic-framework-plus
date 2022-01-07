package com.liangzhicheng.modules.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liangzhicheng.modules.entity.SysPermEntity;
import com.liangzhicheng.modules.entity.dto.SysMenuDTO;

/**
 * <p>
 * 权限信息表 服务类
 * </p>
 *
 * @author liangzhicheng
 */
public interface ISysPermService extends IService<SysPermEntity> {

    /**
     * 根据菜单id获取权限标识
     * @param menuId
     * @return String
     */
    SysPermEntity getPerm(String menuId);

    /**
     * 新增权限
     * @param menuDTO
     * @return SysPermEntity
     */
    SysPermEntity insertPerm(SysMenuDTO menuDTO);

}
