package com.liangzhicheng.common.oss.service;

import com.liangzhicheng.common.exception.TransactionException;
import com.liangzhicheng.common.oss.properties.CloudStorage;
import com.liangzhicheng.common.utils.PrintUtil;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.region.Region;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public class TencentCloudStorageService extends BaseCloudStorageService {

    private COSClient client;

    public TencentCloudStorageService(CloudStorage cloudStorage) {
        this.cloudStorage = cloudStorage;
        //初始化
        init();
    }

    private void init() {
        COSCredentials credentials = new BasicCOSCredentials(
                cloudStorage.getTencentSecretId(),
                cloudStorage.getTencentSecretKey());
        //初始化客户端配置，设置bucket所在的区域，华南：gz，华北：tj，华东：sh
        ClientConfig clientConfig = new ClientConfig(new Region(cloudStorage.getTencentRegion()));
        client = new COSClient(credentials, clientConfig);

    }

    @Override
    public String upload(MultipartFile file) throws Exception {
        String fileName = file.getOriginalFilename();
        String prefix = fileName.substring(fileName.lastIndexOf(".") + 1);
        return upload(file.getInputStream(), String.format("%s.%s", super.getPath(cloudStorage.getTencentPrefix()), prefix));
    }

    @Override
    public String upload(InputStream inputStream, String path) {
        //腾讯云必需要以"/"开头
        if (!path.startsWith("/")) {
            path = String.format("/%s", path);
        }
        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(
                    cloudStorage.getTencentBucketName(),
                    path,
                    inputStream,
                    new ObjectMetadata());
            client.putObject(putObjectRequest);
            return String.format("%s%s", cloudStorage.getTencentDomain(), path);
        } catch (Exception e) {
            PrintUtil.error("[腾讯云] 上传发生异常，异常信息为：{}", e.getMessage());
            throw new TransactionException("上传文件失败，请检查腾讯云配置信息", e);
        }
    }

    @Override
    public String upload(byte[] data, String path) {
        //新版sdk中已弃用
        return null;
    }

    @Override
    public String upload(String filePath, String fileName) {
        return null;
    }

    public void delete(String path) {
        client.deleteObject(cloudStorage.getTencentBucketName(), path);
    }

}
