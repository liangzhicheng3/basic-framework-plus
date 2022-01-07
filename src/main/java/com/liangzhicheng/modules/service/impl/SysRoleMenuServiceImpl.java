package com.liangzhicheng.modules.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liangzhicheng.common.enums.StringEnum;
import com.liangzhicheng.modules.entity.SysRoleMenuEntity;
import com.liangzhicheng.modules.mapper.ISysRoleMenuMapper;
import com.liangzhicheng.modules.service.ISysRoleMenuService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 角色菜单信息表 服务实现类
 * </p>
 *
 * @author liangzhicheng
 */
@Service
public class SysRoleMenuServiceImpl extends ServiceImpl<ISysRoleMenuMapper, SysRoleMenuEntity> implements ISysRoleMenuService {

    /**
     * 根据key，value获取角色菜单列表
     * @param key
     * @param value
     * @return List<SysRoleMenuEntity>
     */
    @Override
    public List<SysRoleMenuEntity> list(String key, String value) {
        return baseMapper.selectList(
                Wrappers.<SysRoleMenuEntity>query()
                        .eq(key, value)
                        .eq(StringEnum.DEL_FLAG.getValue(), StringEnum.ZERO.getValue()));
    }

}
