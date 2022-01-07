package com.liangzhicheng.common.oss.service;

import com.liangzhicheng.common.exception.TransactionException;
import com.liangzhicheng.common.oss.properties.CloudStorage;
import com.liangzhicheng.common.utils.PrintUtil;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.apache.commons.io.IOUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * 七牛云存储服务类
 * @author liangzhicheng
 */
public class QiniuCloudStorageService extends BaseCloudStorageService {

    private UploadManager uploadManager;
    private String token;

    public QiniuCloudStorageService(CloudStorage cloudStorage) {
        this.cloudStorage = cloudStorage;
        //初始化
        init();
    }

    private void init() {
        uploadManager = new UploadManager(new Configuration(Region.region0()));
        token = Auth.create(cloudStorage.getQiniuAccessKey(), cloudStorage.getQiniuSecretKey())
                .uploadToken(cloudStorage.getQiniuBucketName());
    }

    @Override
    public String upload(MultipartFile file) throws Exception {
        String fileName = file.getOriginalFilename();
        String prefix = fileName.substring(fileName.lastIndexOf(".") + 1);
        return upload(file.getBytes(), super.getPath(String.format("%s.%s", cloudStorage.getQiniuPrefix(), prefix)));
    }

    @Override
    public String upload(InputStream inputStream, String path) {
        try {
            byte[] data = IOUtils.toByteArray(inputStream);
            return upload(data, path);
        } catch (IOException e) {
            PrintUtil.error("[七牛云] 上传发生异常，异常信息为：{}", e.getMessage());
            throw new TransactionException("上传文件失败，请检查七牛云配置信息", e);
        }
    }

    @Override
    public String upload(byte[] data, String path) {
        try {
            Response res = uploadManager.put(data, path, token);
            if (!res.isOK()) {
                throw new TransactionException("上传七牛云发生异常：" + res.toString());
            }
        } catch (Exception e) {
            PrintUtil.error("[七牛云] 上传发生异常，异常信息为：{}", e.getMessage());
            throw new TransactionException("上传文件失败，请检查七牛云配置信息", e);
        }
        return String.format("%s%s", cloudStorage.getQiniuDomain(), path);
    }

    @Override
    public String upload(String filePath, String fileName) {
        return null;
    }

}
