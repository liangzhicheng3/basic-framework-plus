package com.liangzhicheng.common.push;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.liangzhicheng.common.constant.Constants;
import com.liangzhicheng.common.push.properties.Push;
import com.liangzhicheng.common.push.service.AliPushService;
import com.liangzhicheng.common.push.service.BasePushService;
import com.liangzhicheng.common.push.service.XingePushService;
import com.liangzhicheng.common.utils.JSONUtil;
import com.liangzhicheng.config.context.SpringContextHolder;
import com.liangzhicheng.modules.entity.SysConfigEntity;
import com.liangzhicheng.modules.service.ISysConfigService;

/**
 * 构建推送工厂类
 * @author liangzhicheng
 */
public final class PushFactory {

    private ISysConfigService configService = SpringContextHolder.getBean("configService");

    private PushFactory() {}

    private static PushFactory instance;

    public static PushFactory getInstance() {
        if(instance == null){
            instance = new PushFactory();
        }
        return instance;
    }

    public BasePushService build() {
        //获取推送配置信息
        SysConfigEntity config = configService.getOne(
                Wrappers.<SysConfigEntity>lambdaQuery()
                        .eq(SysConfigEntity::getKeyName, Constants.PUSH_CONFIG_KEY));
        Push push = JSONUtil.parseObject(config.getValue(), Push.class);
        switch(push.getType()){
            case 1:
                return new XingePushService(push);
            case 2:
                return new AliPushService(push);
        }
        return null;
    }

}
