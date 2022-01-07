package com.liangzhicheng.modules.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liangzhicheng.common.utils.SnowFlakeUtil;
import com.liangzhicheng.common.utils.ThreadUtil;
import com.liangzhicheng.common.utils.ToolUtil;
import com.liangzhicheng.modules.entity.SysPermEntity;
import com.liangzhicheng.modules.entity.SysPermMenuEntity;
import com.liangzhicheng.modules.entity.dto.SysMenuDTO;
import com.liangzhicheng.modules.mapper.ISysPermMapper;
import com.liangzhicheng.modules.service.ISysPermMenuService;
import com.liangzhicheng.modules.service.ISysPermService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * <p>
 * 权限信息表 服务实现类
 * </p>
 *
 * @author liangzhicheng
 */
@Service
public class SysPermServiceImpl extends ServiceImpl<ISysPermMapper, SysPermEntity> implements ISysPermService {

    @Resource
    private ISysPermMenuService permMenuService;

    /**
     * 根据菜单id获取权限标识
     * @param menuId
     * @return String
     */
    @Override
    public SysPermEntity getPerm(String menuId) {
        if(ToolUtil.isNotBlank(menuId)){
            SysPermMenuEntity permMenu = permMenuService.getOne("menu_id", menuId);
            if(ToolUtil.isNotNull(permMenu)){
                return baseMapper.selectById(permMenu.getPermId());
            }
        }
        return null;
    }

    /**
     * 新增权限
     * @param menuDTO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysPermEntity insertPerm(SysMenuDTO menuDTO) {
        String permName = menuDTO.getPermName();
        String expression = menuDTO.getExpression();
        SysPermEntity perm = null;
        if(ToolUtil.isNotBlank(permName, expression)){
            perm = new SysPermEntity();
            perm.setId(SnowFlakeUtil.get().nextId() + "");
            perm.setName(permName);
            perm.setExpression(expression);
            baseMapper.insert(perm);
            //开启线程刷新缓存中权限
            ThreadUtil.getInstance().refreshRolePerm();
        }
        return perm;
    }

}
