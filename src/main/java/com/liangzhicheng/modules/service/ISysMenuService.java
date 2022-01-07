package com.liangzhicheng.modules.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liangzhicheng.modules.entity.SysMenuEntity;
import com.liangzhicheng.modules.entity.dto.SysMenuDTO;
import com.liangzhicheng.modules.entity.vo.SysMenuDescVO;
import com.liangzhicheng.modules.entity.vo.SysMenuVO;

import java.util.List;

/**
 * <p>
 * 菜单信息表 服务类
 * </p>
 *
 * @author liangzhicheng
 */
public interface ISysMenuService extends IService<SysMenuEntity> {

    /**
     * 权限菜单列表
     * @return List<SysMenuVO>
     */
    List<SysMenuVO> listPermMenu();

    /**
     * 根据key，value获取菜单列表
     * @param key
     * @param value
     * @return List<SysMenuEntity>
     */
    List<SysMenuEntity> list(String key, String value);

    /**
     * 获取菜单
     * @param menuDTO
     * @return SysMenuDescVO
     */
    SysMenuDescVO getMenu(SysMenuDTO menuDTO);

    /**
     * 获取一级菜单
     * @return List<SysMenuEntity>
     */
    List<SysMenuEntity> listOne();

    /**
     * 根据一级菜单获取二级菜单
     * @param oneId
     * @return List<SysMenuEntity>
     */
    List<SysMenuEntity> listTwoByOne(String oneId);

    /**
     * 根据二级菜单获取三级菜单
     * @param twoId
     * @return List<SysMenuEntity>
     */
    List<SysMenuEntity> listThreeByTwo(String twoId);

    /**
     * 根据三级菜单获取四级菜单
     * @param threeId
     * @return List<SysMenuEntity>
     */
    List<SysMenuEntity> listFourByThree(String threeId);

    /**
     * 新增菜单
     * @param menuDTO
     */
    void insertMenu(SysMenuDTO menuDTO);

    /**
     * 更新菜单
     * @param menuDTO
     */
    void updateMenu(SysMenuDTO menuDTO);

    /**
     * 删除菜单
     * @param menuDTO
     */
    void deleteMenu(SysMenuDTO menuDTO);

}
