package com.liangzhicheng.modules.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liangzhicheng.common.enums.StringEnum;
import com.liangzhicheng.common.utils.ListUtil;
import com.liangzhicheng.modules.entity.*;
import com.liangzhicheng.modules.mapper.ISysRolePermMapper;
import com.liangzhicheng.modules.service.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>
 * 角色权限信息表 服务实现类
 * </p>
 *
 * @author liangzhicheng
 */
@Service
public class SysRolePermServiceImpl extends ServiceImpl<ISysRolePermMapper, SysRolePermEntity> implements ISysRolePermService {

    @Resource
    private ISysUserService userService;
    @Resource
    private ISysRoleService roleService;
    @Resource
    private ISysPermService permService;
    @Resource
    private ISysRoleUserService roleUserService;
    @Resource
    private ISysRolePermService rolePermService;

    /**
     * 根据key，value获取角色权限列表
     * @param key
     * @param value
     * @return List<SysRolePermEntity>
     */
    @Override
    public List<SysRolePermEntity> list(String key, String value) {
        return baseMapper.selectList(
                Wrappers.<SysRolePermEntity>query()
                        .eq(key, value)
                        .eq(StringEnum.DEL_FLAG.getValue(), StringEnum.ZERO.getValue()));
    }

    /**
     * 权限Map
     * @return Map<String, Object>
     */
    @Override
    public Map<String, Object> mapRolePerm() {
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> roleMap = new HashMap<>();
        Map<String, Object> permMap = new HashMap<>();
        List<SysUserEntity> userList = userService.list(null, null);
        if(ListUtil.sizeGT(userList)){
            List<SysRoleEntity> roleList = null;
            List<SysPermEntity> permList = null;
            List<SysRoleUserEntity> roleUserList = null;
            List<SysRolePermEntity> rolePermList = null;
            for(SysUserEntity user : userList){
                Set<String> roles = new HashSet<>();
                Set<String> perms = new HashSet<>();
                if(StringEnum.ONE.getValue().equals(user.getIsAdmin())){
                    roleList = roleService.list(Wrappers.<SysRoleEntity>lambdaQuery()
                            .eq(SysRoleEntity::getDelFlag, StringEnum.ZERO.getValue()));
                    if(ListUtil.sizeGT(roleList)){
                        for(SysRoleEntity role : roleList){
                            roles.add(role.getName());
                        }
                    }
                    permList = permService.list(Wrappers.<SysPermEntity>lambdaQuery()
                            .eq(SysPermEntity::getDelFlag, StringEnum.ZERO.getValue()));
                    if(ListUtil.sizeGT(permList)){
                        for(SysPermEntity perm : permList){
                            perms.add(perm.getExpression());
                        }
                    }
                }else{
                    roleUserList = roleUserService.list("account_id", user.getId());
                    if(ListUtil.sizeGT(roleUserList)){
                        for(SysRoleUserEntity roleUser : roleUserList){
                            roles.add(roleUser.getRoleName());
                            rolePermList = rolePermService.list("role_id", roleUser.getRoleId());
                            if(ListUtil.sizeGT(rolePermList)){
                                for(SysRolePermEntity rolePerm : rolePermList){
                                    perms.add(rolePerm.getExpression());
                                }
                            }
                        }
                    }
                }
                roleMap.put(user.getId(), roles);
                permMap.put(user.getId(), perms);
            }
        }
        resultMap.put("roleMap", roleMap);
        resultMap.put("permMap", permMap);
        return resultMap;
    }

}
