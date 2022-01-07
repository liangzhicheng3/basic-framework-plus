package com.liangzhicheng.modules.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liangzhicheng.common.enums.StringEnum;
import com.liangzhicheng.common.utils.SnowFlakeUtil;
import com.liangzhicheng.common.utils.ToolUtil;
import com.liangzhicheng.modules.entity.SysMenuEntity;
import com.liangzhicheng.modules.entity.SysPermEntity;
import com.liangzhicheng.modules.entity.SysPermMenuEntity;
import com.liangzhicheng.modules.mapper.ISysPermMenuMapper;
import com.liangzhicheng.modules.service.ISysPermMenuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 权限菜单信息表 服务实现类
 * </p>
 *
 * @author liangzhicheng
 */
@Service
public class SysPermMenuServiceImpl extends ServiceImpl<ISysPermMenuMapper, SysPermMenuEntity> implements ISysPermMenuService {

    /**
     * 根据key，value获取权限菜单
     * @param key
     * @param value
     * @return SysPermMenuEntity
     */
    @Override
    public SysPermMenuEntity getOne(String key, String value) {
        return baseMapper.selectOne(Wrappers.<SysPermMenuEntity>query()
                        .eq(key, value)
                        .eq(StringEnum.DEL_FLAG.getValue(), StringEnum.ZERO.getValue()));
    }

    /**
     * 新增权限菜单
     * @param perm
     * @param menu
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertPermMenu(SysPermEntity perm, SysMenuEntity menu) {
        if(ToolUtil.isNotNull(perm) && ToolUtil.isNotNull(menu)){
            SysPermMenuEntity permMenu = new SysPermMenuEntity(SnowFlakeUtil.get().nextId() + "",
                    perm.getId(), perm.getName(), menu.getId(), menu.getName());
            baseMapper.insert(permMenu);
        }
    }

}
