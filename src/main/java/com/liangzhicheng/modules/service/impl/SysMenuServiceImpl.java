package com.liangzhicheng.modules.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liangzhicheng.common.constant.ApiConstant;
import com.liangzhicheng.common.enums.StringEnum;
import com.liangzhicheng.common.utils.*;
import com.liangzhicheng.modules.entity.SysMenuEntity;
import com.liangzhicheng.modules.entity.SysPermEntity;
import com.liangzhicheng.modules.entity.SysPermMenuEntity;
import com.liangzhicheng.modules.entity.SysRolePermEntity;
import com.liangzhicheng.modules.entity.dto.SysMenuDTO;
import com.liangzhicheng.modules.entity.vo.*;
import com.liangzhicheng.modules.mapper.ISysMenuMapper;
import com.liangzhicheng.modules.service.ISysMenuService;
import com.liangzhicheng.modules.service.ISysPermMenuService;
import com.liangzhicheng.modules.service.ISysPermService;
import com.liangzhicheng.modules.service.ISysRolePermService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 菜单信息表 服务实现类
 * </p>
 *
 * @author liangzhicheng
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<ISysMenuMapper, SysMenuEntity> implements ISysMenuService {

    @Resource
    private ISysPermService permService;
    @Resource
    private ISysPermMenuService permMenuService;
    @Resource
    private ISysRolePermService rolePermService;

    /**
     * 权限菜单列表
     * @return List<SysMenuVO>
     */
    @Override
    public List<SysMenuVO> listPermMenu() {
        List<SysMenuVO> oneVOList = new ArrayList<>();
        List<SysMenuEntity> oneList = list("level", StringEnum.ONE.getValue());
        if(ListUtil.sizeGT(oneList)){
            SysMenuVO oneVO = null;
            SysMenuTwoVO twoVO = null;
            SysMenuThreeVO threeVO = null;
            SysMenuFourVO fourVO = null;
            List<SysMenuEntity> twoList = null;
            List<SysMenuTwoVO> twoVOList = null;
            List<SysMenuEntity> threeList = null;
            List<SysMenuThreeVO> threeVOList = null;
            List<SysMenuEntity> fourList = null;
            List<SysMenuFourVO> fourVOList = null;
            for(SysMenuEntity one : oneList) {
                oneVO = BeansUtil.copyEntity(one, SysMenuVO.class);
                //获取一级菜单权限标识
                SysPermEntity onePerm = permService.getPerm(oneVO.getId());
                if(ToolUtil.isNotNull(onePerm)){
                    oneVO.setPermId(onePerm.getId());
                    oneVO.setExpression(onePerm.getExpression());
                }
                twoList = listTwoByOne(oneVO.getId());
                if(ListUtil.sizeGT(twoList)){
                    twoVOList = new ArrayList<>(twoList.size());
                    for(SysMenuEntity two : twoList){
                        twoVO = BeansUtil.copyEntity(two, SysMenuTwoVO.class);
                        //获取二级菜单权限标识
                        SysPermEntity twoPerm = permService.getPerm(twoVO.getId());
                        if(ToolUtil.isNotNull(twoPerm)){
                            twoVO.setPermId(twoPerm.getId());
                            twoVO.setExpression(twoPerm.getExpression());
                        }
                        threeList = listThreeByTwo(twoVO.getId());
                        if(ListUtil.sizeGT(threeList)){
                            threeVOList = new ArrayList<>(threeList.size());
                            for(SysMenuEntity three : threeList){
                                threeVO = BeansUtil.copyEntity(three, SysMenuThreeVO.class);
                                //获取三级菜单权限标识
                                SysPermEntity threePerm = permService.getPerm(threeVO.getId());
                                if(ToolUtil.isNotNull(threePerm)){
                                    threeVO.setPermId(threePerm.getId());
                                    threeVO.setExpression(threePerm.getExpression());
                                }
                                fourList = listFourByThree(threeVO.getId());
                                if(ListUtil.sizeGT(fourList)){
                                    fourVOList = new ArrayList<>(fourList.size());
                                    for(SysMenuEntity four : fourList){
                                        fourVO = BeansUtil.copyEntity(four, SysMenuFourVO.class);
                                        //获取四级菜单权限标识
                                        SysPermEntity fourPerm = permService.getPerm(fourVO.getId());
                                        if(ToolUtil.isNotNull(fourPerm)){
                                            fourVO.setPermId(fourPerm.getId());
                                            fourVO.setExpression(fourPerm.getExpression());
                                        }
                                        fourVOList.add(fourVO);
                                        threeVO.setChildrenList(fourVOList);
                                    }
                                }
                                threeVOList.add(threeVO);
                                twoVO.setChildrenList(threeVOList);
                            }
                        }
                        twoVOList.add(twoVO);
                        oneVO.setChildrenList(twoVOList);
                    }
                }
                oneVOList.add(oneVO);
            }
        }
        return oneVOList;
    }

    /**
     * 根据key，value获取菜单列表
     * @param key
     * @param value
     * @return List<SysMenuEntity>
     */
    @Override
    public List<SysMenuEntity> list(String key, String value) {
        QueryWrapper<SysMenuEntity> wrapperMenu = Wrappers.<SysMenuEntity>query();
        if(ToolUtil.isNotBlank(key, value)){
            wrapperMenu.eq(key, value);
        }
        return baseMapper.selectList(
                wrapperMenu.eq(StringEnum.DEL_FLAG.getValue(), StringEnum.ZERO.getValue())
                        .orderByAsc("rank"));
    }

    /**
     * 获取菜单
     * @param menuDTO
     * @return SysMenuDescVO
     */
    @Override
    public SysMenuDescVO getMenu(SysMenuDTO menuDTO) {
        SysMenuEntity menu = baseMapper.selectById(menuDTO.getId());
        AssertUtil.isFalse(ToolUtil.isNull(menu), "菜单不存在");
        SysMenuDescVO menuDescVO = BeansUtil.copyEntity(menu, SysMenuDescVO.class);
        SysPermEntity perm = permService.getPerm(menuDescVO.getId());
        if(ToolUtil.isNotNull(perm)){
            menuDescVO.setPermId(perm.getId());
            menuDescVO.setPermName(perm.getName());
            menuDescVO.setExpression(perm.getExpression());
        }
        return menuDescVO;
    }

    /**
     * 获取一级菜单
     * @return List<SysMenuEntity>
     */
    @Override
    public List<SysMenuEntity> listOne() {
        return baseMapper.selectList(Wrappers.<SysMenuEntity>lambdaQuery()
                .eq(SysMenuEntity::getLevel, StringEnum.ONE.getValue())
                .eq(SysMenuEntity::getDelFlag, StringEnum.ZERO.getValue())
                .orderByAsc(SysMenuEntity::getRank));
    }

    /**
     * 根据一级菜单获取二级菜单
     * @param oneId
     * @return List<SysMenuEntity>
     */
    @Override
    public List<SysMenuEntity> listTwoByOne(String oneId) {
        return baseMapper.selectList(Wrappers.<SysMenuEntity>lambdaQuery()
                .eq(SysMenuEntity::getParentId, oneId)
                .eq(SysMenuEntity::getLevel, StringEnum.TWO.getValue())
                .eq(SysMenuEntity::getDelFlag, StringEnum.ZERO.getValue())
                .orderByAsc(SysMenuEntity::getRank));
    }

    /**
     * 根据二级菜单获取三级菜单
     * @param twoId
     * @return List<SysMenuEntity>
     */
    public List<SysMenuEntity> listThreeByTwo(String twoId) {
        return baseMapper.selectList(Wrappers.<SysMenuEntity>lambdaQuery()
                .eq(SysMenuEntity::getParentId, twoId)
                .eq(SysMenuEntity::getLevel, StringEnum.THREE.getValue())
                .eq(SysMenuEntity::getDelFlag, StringEnum.ZERO.getValue())
                .orderByAsc(SysMenuEntity::getRank));
    }

    /**
     * 根据三级菜单获取四级菜单
     * @param threeId
     * @return List<SysMenuEntity>
     */
    public List<SysMenuEntity> listFourByThree(String threeId) {
        return baseMapper.selectList(Wrappers.<SysMenuEntity>lambdaQuery()
                .eq(SysMenuEntity::getParentId, threeId)
                .eq(SysMenuEntity::getLevel, StringEnum.FOUR.getValue())
                .eq(SysMenuEntity::getDelFlag, StringEnum.ZERO.getValue())
                .orderByAsc(SysMenuEntity::getRank));
    }

    /**
     * 新增菜单
     * @param menuDTO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertMenu(SysMenuDTO menuDTO) {
        String parentId = menuDTO.getParentId();
        String name = menuDTO.getName();
        String component = menuDTO.getComponent();
        String routerPath = menuDTO.getRouterPath();
        String redirect = menuDTO.getRedirect();
        String isHide = menuDTO.getIsHide();
        Integer rank = menuDTO.getRank();
        AssertUtil.isFalse(ToolUtil.isBlank(name), ApiConstant.PARAM_IS_NULL);
        AssertUtil.isFalse(ToolUtil.notIn(isHide, StringEnum.ZERO.getValue(),
                StringEnum.ONE.getValue()), ApiConstant.PARAM_ERROR);
        SysMenuEntity menu = null;
        if(ToolUtil.isBlank(parentId)){
            menu = new SysMenuEntity();
            menu.setLevel(StringEnum.ONE.getValue());
        }else{
            menu = baseMapper.selectById(parentId);
            AssertUtil.isFalse(ToolUtil.isNull(menu), "菜单不存在");
            if(StringEnum.ONE.getValue().equals(menu.getLevel())){
                menu = new SysMenuEntity();
                menu.setLevel(StringEnum.TWO.getValue());
            }else if(StringEnum.TWO.getValue().equals(menu.getLevel())){
                menu = new SysMenuEntity();
                menu.setLevel(StringEnum.THREE.getValue());
            }else if(StringEnum.THREE.getValue().equals(menu.getLevel())){
                menu = new SysMenuEntity();
                menu.setLevel(StringEnum.FOUR.getValue());
            }
        }
        menu.setId(SnowFlakeUtil.get().nextId() + "");
        menu.setParentId(parentId);
        AssertUtil.isFalse(name.length() > 30, "标题字数过长");
        menu.setName(name);
        menu.setComponent(component);
        menu.setRouterPath(routerPath);
        menu.setRedirect(redirect);
        menu.setIsHide(isHide);
        menu.setRank(rank);
        baseMapper.insert(menu);
        //开启线程刷新缓存中菜单
        ThreadUtil.getInstance().refreshPermMenu();
        /**
         * 新增权限记录
         * 用于接口贴@RequiresPermissions注解拦截
         * 如：@RequiresPermissions("userServerController-listUser或/user/info")
         * name：权限名称(用户列表)
         * expression：表达式(userServerController-listUser或/user/info)
         */
        SysPermEntity perm = permService.insertPerm(menuDTO);
        if(ToolUtil.isNotNull(perm)){
            //新增权限菜单记录
            permMenuService.insertPermMenu(perm, menu);
        }
    }

    /**
     * 更新菜单
     * @param menuDTO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMenu(SysMenuDTO menuDTO) {
        String id = menuDTO.getId();
        String name = menuDTO.getName();
        String component = menuDTO.getComponent();
        String routerPath = menuDTO.getRouterPath();
        String redirect = menuDTO.getRedirect();
        String isHide = menuDTO.getIsHide();
        Integer rank = menuDTO.getRank();
        //权限参数
        String permId = menuDTO.getPermId();
        String permName = menuDTO.getPermName();
        String expression = menuDTO.getExpression();
        SysMenuEntity menu = baseMapper.selectById(id);
        AssertUtil.isFalse(ToolUtil.isNull(menu), "菜单不存在");
        if(ToolUtil.isNotBlank(name)){
            AssertUtil.isFalse(name.length() > 30, "标题字数过长");
            menu.setName(name);
        }
        if(ToolUtil.isNotBlank(component)){
            menu.setComponent(component);
        }
        if(ToolUtil.isNotBlank(routerPath)){
            menu.setRouterPath(routerPath);
        }
        if(ToolUtil.isNotBlank(redirect)){
            menu.setRedirect(redirect);
        }
        if(ToolUtil.isNotBlank(isHide)){
            AssertUtil.isFalse(ToolUtil.notIn(isHide, StringEnum.ZERO.getValue(),
                    StringEnum.ONE.getValue()), ApiConstant.PARAM_ERROR);
            menu.setIsHide(isHide);
        }
        if(ToolUtil.isNotBlank(String.valueOf(rank))){
            AssertUtil.isFalse(rank < 0, ApiConstant.PARAM_ERROR);
            menu.setRank(rank);
        }
        baseMapper.updateById(menu);
        //开启线程刷新缓存中菜单
        ThreadUtil.getInstance().refreshPermMenu();
        //权限处理
        SysPermEntity perm = permService.getById(permId);
        if(ToolUtil.isNull(perm)){
            perm = new SysPermEntity();
            perm.setId(SnowFlakeUtil.get().nextId() + "");
        }
        perm.setName(permName);
        perm.setExpression(expression);
        permService.saveOrUpdate(perm);
        //权限菜单处理
        SysPermMenuEntity permMenu = permMenuService.getOne("perm_id", perm.getId());
        if(ToolUtil.isNull(permMenu)){
            permMenu = new SysPermMenuEntity();
            permMenu.setId(SnowFlakeUtil.get().nextId() + "");
            permMenu.setPermId(perm.getId());
            permMenu.setMenuId(menu.getId());
        }
        permMenu.setPermName(permName);
        permMenu.setMenuName(menu.getName());
        permMenuService.saveOrUpdate(permMenu);
        //角色权限处理
        List<SysRolePermEntity> rolePermList = rolePermService.list("perm_id", perm.getId());
        if(ListUtil.sizeGT(rolePermList)){
            for(SysRolePermEntity rolePerm: rolePermList) {
                rolePerm.setPermName(permName);
                rolePerm.setExpression(expression);
            }
            rolePermService.updateBatchById(rolePermList);
        }
    }

    /**
     * 删除菜单
     * @param menuDTO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMenu(SysMenuDTO menuDTO) {
        SysMenuEntity menu = baseMapper.selectById(menuDTO.getId());
        AssertUtil.isFalse(ToolUtil.isNull(menu), "菜单不存在");
        if(StringEnum.ONE.getValue().equals(menu.getLevel())){
            //更新二级菜单平台删除标记
            SysMenuEntity two = updateDelFlag(StringEnum.TWO.getValue(), menu.getId());
            if(ToolUtil.isNotNull(two)){
                //更新三级菜单平台删除标记
                SysMenuEntity three = updateDelFlag(StringEnum.THREE.getValue(), two.getId());
                if(ToolUtil.isNotNull(three)){
                    //更新四级菜单平台删除标记
                    updateDelFlag(StringEnum.FOUR.getValue(), three.getId());
                }
            }
        }else if(StringEnum.TWO.getValue().equals(menu.getLevel())){
            //更新三级菜单平台删除标记
            SysMenuEntity three = updateDelFlag(StringEnum.THREE.getValue(), menu.getId());
            if(ToolUtil.isNotNull(three)){
                //更新四级菜单平台删除标记
                updateDelFlag(StringEnum.FOUR.getValue(), three.getId());
            }
        }else if(StringEnum.THREE.getValue().equals(menu.getLevel())){
            //更新四级菜单平台删除标记
            updateDelFlag(StringEnum.FOUR.getValue(), menu.getId());
        }
        menu.setDelFlag(StringEnum.ONE.getValue());
        baseMapper.updateById(menu);
        //开启线程刷新缓存中菜单
        ThreadUtil.getInstance().refreshPermMenu();
        //更新权限菜单平台删除标记
        SysPermMenuEntity permMenu = permMenuService.getOne("menu_id", menu.getId());
        if(ToolUtil.isNotNull(permMenu)){
            permMenu.setDelFlag(StringEnum.ONE.getValue());
            permMenuService.updateById(permMenu);
        }
    }

    /**
     * 更新平台删除标记
     * @param level
     * @param pid
     * @return SysMenuEntity
     */
    @Transactional(rollbackFor = Exception.class)
    public SysMenuEntity updateDelFlag(String level, String pid){
        SysMenuEntity menu = baseMapper.selectOne(
                Wrappers.<SysMenuEntity>lambdaQuery()
                .eq(SysMenuEntity::getLevel, level)
                .eq(SysMenuEntity::getParentId, pid)
                .eq(SysMenuEntity::getDelFlag, StringEnum.ZERO.getValue()));
        if(ToolUtil.isNotNull(menu)){
            menu.setDelFlag(StringEnum.ONE.getValue());
            baseMapper.updateById(menu);
        }
        return menu;
    }

}
