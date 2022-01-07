package com.liangzhicheng.modules.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liangzhicheng.modules.entity.SysRolePermEntity;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 角色权限信息表 服务类
 * </p>
 *
 * @author liangzhicheng
 */
public interface ISysRolePermService extends IService<SysRolePermEntity> {

    /**
     * 根据key，value获取角色权限列表
     * @param key
     * @param value
     * @return List<SysRolePermEntity>
     */
    List<SysRolePermEntity> list(String key, String value);

    /**
     * 权限Map
     * @return Map<String, Object>
     */
    Map<String, Object> mapRolePerm();

}
