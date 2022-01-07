package com.liangzhicheng.common.oss;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.liangzhicheng.common.constant.Constants;
import com.liangzhicheng.common.oss.properties.CloudStorage;
import com.liangzhicheng.common.oss.service.*;
import com.liangzhicheng.common.utils.JSONUtil;
import com.liangzhicheng.config.context.SpringContextHolder;
import com.liangzhicheng.modules.entity.SysConfigEntity;
import com.liangzhicheng.modules.service.ISysConfigService;

/**
 * 构建云存储工厂类
 * @author liangzhicheng
 */
public final class OSSFactory {

    private ISysConfigService configService = SpringContextHolder.getBean("configService");

    private OSSFactory() {}

    private static OSSFactory instance;

    public static OSSFactory getInstance() {
        if(instance == null){
            instance = new OSSFactory();
        }
        return instance;
    }

    public BaseCloudStorageService build(){
        //获取云存储配置信息
        SysConfigEntity config = configService.getOne(
                Wrappers.<SysConfigEntity>lambdaQuery()
                        .eq(SysConfigEntity::getKeyName, Constants.CLOUD_STORAGE_CONFIG_KEY));
        CloudStorage cloudStorage = JSONUtil.parseObject(config.getValue(), CloudStorage.class);
        switch(cloudStorage.getType()){
            case 1:
                return new QiniuCloudStorageService(cloudStorage);
            case 2:
                return new AliCloudStorageService(cloudStorage);
            case 3:
                return new TencentCloudStorageService(cloudStorage);
            case 4:
                return new ServerStorageService(cloudStorage);
        }
        return null;
    }

}
