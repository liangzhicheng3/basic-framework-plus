package com.liangzhicheng.modules.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liangzhicheng.modules.entity.SysConfigEntity;
import com.liangzhicheng.modules.mapper.ISysConfigMapper;
import com.liangzhicheng.modules.service.ISysConfigService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 配置信息表 服务实现类
 * </p>
 *
 * @author liangzhicheng
 */
@Service("configService")
public class SysConfigServiceImpl extends ServiceImpl<ISysConfigMapper, SysConfigEntity> implements ISysConfigService {

}
