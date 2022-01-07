package com.liangzhicheng.modules.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.liangzhicheng.modules.entity.SysUserEntity;

import java.util.List;

/**
 * <p>
 * 账号信息表 Mapper 接口
 * </p>
 *
 * @author liangzhicheng
 */
public interface ISysUserMapper extends BaseMapper<SysUserEntity> {

    List<String> selectListByUserMenu(String accountId);

}
