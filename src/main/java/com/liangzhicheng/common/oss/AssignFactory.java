package com.liangzhicheng.common.oss;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.gson.Gson;
import com.liangzhicheng.common.constant.Constants;
import com.liangzhicheng.common.exception.TransactionException;
import com.liangzhicheng.common.oss.properties.CloudStorage;
import com.liangzhicheng.common.utils.JSONUtil;
import com.liangzhicheng.common.utils.PrintUtil;
import com.liangzhicheng.config.context.SpringContextHolder;
import com.liangzhicheng.modules.entity.SysConfigEntity;
import com.liangzhicheng.modules.service.ISysConfigService;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;

/**
 * 指定上传工厂类
 * @author liangzhicheng
 */
public class AssignFactory {

    private ISysConfigService configService = SpringContextHolder.getBean("configService");

    private AssignFactory() {}

    private static AssignFactory instance;

    public static AssignFactory getInstance() {
        if(instance == null){
            instance = new AssignFactory();
        }
        return instance;
    }

    public String upload(String filePath, String fileName){
        //获取云存储配置信息
        SysConfigEntity config = configService.getOne(
                Wrappers.<SysConfigEntity>lambdaQuery()
                        .eq(SysConfigEntity::getKeyName, Constants.CLOUD_STORAGE_CONFIG_KEY));
        CloudStorage cloudStorage = JSONUtil.parseObject(config.getValue(), CloudStorage.class);
        UploadManager uploadManager = new UploadManager(new Configuration(Region.region0()));
        String token = Auth.create(cloudStorage.getQiniuAccessKey(), cloudStorage.getQiniuSecretKey())
                //insertOnly 如果希望只能上传指定key的文件，并且不允许修改，那么可以将下面的 insertOnly 属性值设为 1
                //覆盖上传，访问时需要刷新缓存?v=时间戳
                .uploadToken(cloudStorage.getQiniuBucketName(), fileName, 3600, new StringMap().put("insertOnly", 0));
        try {
            Response response = uploadManager.put(filePath, fileName, token);
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            return String.format("%s%s", cloudStorage.getQiniuDomain(), putRet.key);
        } catch (QiniuException e) {
            PrintUtil.info("[七牛云上传] 发生异常，异常信息为：{}", e.getMessage());
            throw new TransactionException("上传文件失败，请检查七牛云配置信息", e);
        }
    }

}
