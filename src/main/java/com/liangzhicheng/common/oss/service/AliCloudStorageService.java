package com.liangzhicheng.common.oss.service;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.liangzhicheng.common.exception.TransactionException;
import com.liangzhicheng.common.oss.properties.CloudStorage;
import com.liangzhicheng.common.utils.PrintUtil;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * 阿里云存储服务类
 * @author liangzhicheng
 */
public class AliCloudStorageService extends BaseCloudStorageService {

    private OSS client;

    public AliCloudStorageService(CloudStorage cloudStorage) {
        this.cloudStorage = cloudStorage;
        //初始化
        init();
    }

    private void init() {
//        client = new OSSClient(cloudStorage.getAliEndPoint(), cloudStorage.getAliAccessKey(),
//                cloudStorage.getAliSecretKey());
        client = new OSSClientBuilder().build(
                cloudStorage.getAliEndPoint(),
                cloudStorage.getAliAccessKey(),
                cloudStorage.getAliSecretKey());
    }

    @Override
    public String upload(MultipartFile file) throws Exception {
        String fileName = file.getOriginalFilename();
        String prefix = fileName.substring(fileName.lastIndexOf(".") + 1);
        return upload(file.getBytes(), super.getPath(String.format("%s.%s", cloudStorage.getAliPrefix(), prefix)));
    }

    @Override
    public String upload(InputStream inputStream, String path) {
        try {
            client.putObject(cloudStorage.getAliBucketName(), path, inputStream);
        } catch (Exception e) {
            PrintUtil.error("[阿里云] 上传发生异常，异常信息为：{}", e.getMessage());
            throw new TransactionException("上传文件失败，请检查阿里云配置信息", e);
        }
        return String.format("%s/%s", cloudStorage.getAliDomain(), path);
    }

    @Override
    public String upload(byte[] data, String path) {
        return upload(new ByteArrayInputStream(data), path);
    }

    @Override
    public String upload(String filePath, String fileName) {
        return null;
    }

}
