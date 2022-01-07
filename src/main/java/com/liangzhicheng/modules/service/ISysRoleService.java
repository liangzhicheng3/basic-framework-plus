package com.liangzhicheng.modules.service;

import com.liangzhicheng.modules.entity.SysRoleEntity;
import com.liangzhicheng.modules.entity.dto.SysRoleDTO;
import com.liangzhicheng.modules.entity.vo.SysRoleDescVO;
import org.springframework.data.domain.Pageable;

import java.util.Map;

/**
 * <p>
 * 角色信息表 服务类
 * </p>
 *
 * @author liangzhicheng
 */
public interface ISysRoleService extends IBaseService<SysRoleEntity> {

    /**
     * 角色管理
     * @param roleDTO
     * @return Map<String, Object>
     */
    Map<String, Object> listRole(SysRoleDTO roleDTO, Pageable pageable);

    /**
     * 获取角色
     * @param roleDTO
     * @return SysRoleVO
     */
    SysRoleDescVO getRole(SysRoleDTO roleDTO);

    /**
     * 保存角色
     * @param roleDTO
     */
    void saveRole(SysRoleDTO roleDTO);

    /**
     * 删除角色
     * @param roleDTO
     */
    void deleteRole(SysRoleDTO roleDTO);

}
