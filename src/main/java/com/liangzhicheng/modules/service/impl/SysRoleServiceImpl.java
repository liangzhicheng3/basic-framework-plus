package com.liangzhicheng.modules.service.impl;

import com.github.pagehelper.PageInfo;
import com.liangzhicheng.common.enums.StringEnum;
import com.liangzhicheng.common.response.WrapperHelper;
import com.liangzhicheng.common.utils.*;
import com.liangzhicheng.modules.entity.*;
import com.liangzhicheng.modules.entity.dto.SysRoleDTO;
import com.liangzhicheng.modules.entity.query.SysRoleQueryCondition;
import com.liangzhicheng.modules.entity.vo.SysRoleDescVO;
import com.liangzhicheng.modules.entity.vo.SysRoleVO;
import com.liangzhicheng.modules.mapper.ISysRoleMapper;
import com.liangzhicheng.modules.service.*;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 角色信息表 服务实现类
 * </p>
 *
 * @author liangzhicheng
 */
@Service
public class SysRoleServiceImpl extends BaseServiceImpl<ISysRoleMapper, SysRoleEntity> implements ISysRoleService {

    @Resource
    private ISysMenuService menuService;
    @Resource
    private ISysRoleMenuService roleMenuService;
    @Resource
    private ISysPermService permService;
    @Resource
    private ISysRolePermService rolePermService;
    @Resource
    private ISysRoleUserService roleUserService;

    /**
     * 角色管理
     * @param roleDTO
     * @return Map<String, Object>
     */
    @Override
    public Map<String, Object> listRole(SysRoleDTO roleDTO, Pageable pageable) {
        SysRoleQueryCondition roleQuery = new SysRoleQueryCondition(roleDTO);
        super.pageHandle(pageable, roleQuery.getPageNum(), roleQuery.getPageSize());
        List<SysRoleEntity> roleList = baseMapper.selectList(
                WrapperHelper.getInstance().buildCondition(SysRoleEntity.class, roleQuery));
        PageInfo pageInfo = new PageInfo<>();
        List<?> records = null;
        if(ListUtil.sizeGT(roleList)){
            pageInfo = new PageInfo<>(roleList);
            records = BeansUtil.copyList(pageInfo.getList(), SysRoleVO.class);
        }
        return super.pageResult(records, pageInfo);
    }

    /**
     * 获取角色
     * @param roleDTO
     * @return SysRoleVO
     */
    @Override
    public SysRoleDescVO getRole(SysRoleDTO roleDTO) {
        SysRoleEntity role = baseMapper.selectById(roleDTO.getId());
        AssertUtil.isFalse(ToolUtil.isNull(role), "角色不存在");
        SysRoleDescVO roleDescVO = BeansUtil.copyEntity(role, SysRoleDescVO.class);
        List<SysRoleMenuEntity> roleMenuList = roleMenuService.list("role_id", roleDescVO.getId());
        if(ListUtil.sizeGT(roleMenuList)){
            List<String> menuIds = new ArrayList<>(roleMenuList.size());
            for(SysRoleMenuEntity roleMenu : roleMenuList){
                menuIds.add(roleMenu.getMenuId());
            }
            roleDescVO.setMenuIds(menuIds);
        }
        List<SysRolePermEntity> rolePermList = rolePermService.list("role_id", roleDescVO.getId());
        if(ListUtil.sizeGT(rolePermList)){
            List<String> permIds = new ArrayList<>(rolePermList.size());
            for(SysRolePermEntity rolePerm : rolePermList){
                permIds.add(rolePerm.getPermId());
            }
            roleDescVO.setPermIds(permIds);
        }
        return roleDescVO;
    }

    /**
     * 保存角色
     * @param roleDTO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRole(SysRoleDTO roleDTO) {
        String id = roleDTO.getId();
        String name = roleDTO.getName();
        String description = roleDTO.getDescription();
        String menuIds = roleDTO.getMenuIds();
        String permIds = roleDTO.getPermIds();
        SysRoleEntity role = baseMapper.selectById(id);
        if(ToolUtil.isNull(role)){
            role = new SysRoleEntity();
            role.setId(SnowFlakeUtil.get().nextId() + "");
        }
        if(ToolUtil.isNotBlank(name)){
            AssertUtil.isFalse(name.length() > 30, "角色名称字数过长");
            role.setName(name);
        }
        if(ToolUtil.isNotBlank(description)){
            AssertUtil.isFalse(description.length() > 255, "角色描述字数过长");
            role.setDescription(description);
        }
        //角色菜单，角色权限处理
        if(ToolUtil.isNotBlank(menuIds, permIds)){
            //角色菜单
            List<SysRoleMenuEntity> roleMenuList = null;
            //角色权限
            List<SysRolePermEntity> rolePermList = null;
            String[] arrayMenuId = menuIds.split(",");
            String[] arrayPermId = permIds.split(",");
            if(arrayMenuId != null && arrayMenuId.length > 0){
                SysMenuEntity menu = null;
                SysRoleMenuEntity roleMenu = null;
                roleMenuList = new ArrayList<>();
                for(String menuId : arrayMenuId){
                    menu = menuService.getById(menuId);
                    AssertUtil.isFalse(ToolUtil.isNull(menu), "菜单记录不存在");
                    roleMenu = new SysRoleMenuEntity(SnowFlakeUtil.get().nextId() + "",
                            role.getId(), role.getName(), menuId, menu.getName());
                    roleMenuList.add(roleMenu);
                }
            }
            if(arrayPermId != null && arrayPermId.length > 0){
                SysPermEntity perm = null;
                SysRolePermEntity rolePerm = null;
                rolePermList = new ArrayList<>();
                for(String permId : arrayPermId){
                    perm = permService.getById(permId);
                    AssertUtil.isFalse(ToolUtil.isNull(perm), "权限记录不存在");
                    rolePerm = new SysRolePermEntity(SnowFlakeUtil.get().nextId() + "",
                            role.getId(), permId, perm.getName(), perm.getExpression());
                    rolePermList.add(rolePerm);
                }
            }
            //保存前做删除操作
            deleteRolePermMenu(role.getId());
            roleMenuService.saveBatch(roleMenuList);
            rolePermService.saveBatch(rolePermList);
        }else{
            //去除勾选做删除操作
            deleteRolePermMenu(role.getId());
        }
        saveOrUpdate(role);
        //开启线程刷新缓存中权限
        ThreadUtil.getInstance().refreshRolePerm();
    }

    /**
     * 删除角色
     * @param roleDTO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRole(SysRoleDTO roleDTO) {
        SysRoleEntity role = baseMapper.selectById(roleDTO.getId());
        AssertUtil.isFalse(ToolUtil.isNull(role), "角色不存在");
        String id = role.getId();
        //删除角色权限，角色菜单
        deleteRolePermMenu(id);
        //删除角色用户记录
        List<SysRoleUserEntity> roleUserList = roleUserService.list("role_id", id);
        if(ListUtil.sizeGT(roleUserList)){
            for(SysRoleUserEntity roleUser : roleUserList){
                roleUser.setDelFlag(StringEnum.ONE.getValue());
            }
            roleUserService.updateBatchById(roleUserList);
        }
        role.setDelFlag(StringEnum.ONE.getValue());
        baseMapper.updateById(role);
        //开启线程刷新缓存中权限
        ThreadUtil.getInstance().refreshRolePerm();
    }

    /**
     * 删除角色权限，角色菜单
     * @param roleId
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteRolePermMenu(String roleId){
        rolePermService.lambdaUpdate()
                .eq(SysRolePermEntity::getRoleId, roleId)
                .eq(SysRolePermEntity::getDelFlag, StringEnum.ZERO.getValue())
                .set(SysRolePermEntity::getDelFlag, StringEnum.ONE.getValue())
                .update();
        roleMenuService.lambdaUpdate()
                .eq(SysRoleMenuEntity::getRoleId, roleId)
                .eq(SysRoleMenuEntity::getDelFlag, StringEnum.ZERO.getValue())
                .set(SysRoleMenuEntity::getDelFlag, StringEnum.ONE.getValue())
                .update();
    }

}
