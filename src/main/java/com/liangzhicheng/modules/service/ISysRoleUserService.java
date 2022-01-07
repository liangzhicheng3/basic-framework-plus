package com.liangzhicheng.modules.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liangzhicheng.modules.entity.SysRoleUserEntity;

import java.util.List;

/**
 * <p>
 * 角色用户表 服务类
 * </p>
 *
 * @author liangzhicheng
 */
public interface ISysRoleUserService extends IService<SysRoleUserEntity> {

    /**
     * 根据key，value获取角色用户列表
     * @param key
     * @param value
     * @return List<SysRoleUserEntity>
     */
    List<SysRoleUserEntity> list(String key, String value);

    /**
     * 测试实时用户角色数
     * @param connectId
     */
    void testOnlineRoleUser(String connectId);

}
