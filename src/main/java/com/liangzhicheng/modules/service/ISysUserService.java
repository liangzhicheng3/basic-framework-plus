package com.liangzhicheng.modules.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.liangzhicheng.modules.entity.SysUserEntity;
import com.liangzhicheng.modules.entity.dto.SysUserDTO;
import com.liangzhicheng.modules.entity.vo.SysPersonInfoVO;
import com.liangzhicheng.modules.entity.vo.SysUserDescVO;
import com.liangzhicheng.modules.entity.vo.SysUserLoginVO;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 账号信息表 服务类
 * </p>
 *
 * @author liangzhicheng
 */
public interface ISysUserService extends IBaseService<SysUserEntity> {

    /**
     * 登录
     * @param userDTO
     * @param request
     * @return SysUserLoginVO
     */
    SysUserLoginVO login(SysUserDTO userDTO, HttpServletRequest request);

    /**
     * 退出登录
     * @param request
     */
    void logOut(HttpServletRequest request);

    /**
     * 更新当前登录用户头像
     * @param userDTO
     * @param request
     * @return SysPersonInfoVO
     */
    SysPersonInfoVO updateAvatar(SysUserDTO userDTO, HttpServletRequest request);

    /**
     * 更新当前登录用户密码
     * @param userDTO
     * @param request
     */
    void updatePassword(SysUserDTO userDTO, HttpServletRequest request);

    /**
     * 账号管理列表
     * @param userDTO
     * @return IPage
     */
    IPage listAccount(SysUserDTO userDTO);

    /**
     * 根据key，value获取用户列表
     * @param key
     * @param value
     * @return List<SysUserEntity>
     */
    List<SysUserEntity> list(String key, String value);

    /**
     * 获取账号
     * @param userDTO
     * @return SysUserDescVO
     */
    SysUserDescVO getAccount(SysUserDTO userDTO);

    /**
     * 保存账号
     * @param userDTO
     */
    void saveAccount(SysUserDTO userDTO);

    /**
     * 重置密码
     * @param userDTO
     */
    void resetPassword(SysUserDTO userDTO);

    /**
     * 删除账号
     * @param userDTO
     */
    void deleteAccount(SysUserDTO userDTO);

    SysUserEntity getByCache(String userId);

    /**
     * 测试自定义排序查询列表
     * @param sysUserDTO
     * @param pageable
     * @return
     */
    Map<String, Object> testUserList(SysUserDTO sysUserDTO, Pageable pageable);

}
