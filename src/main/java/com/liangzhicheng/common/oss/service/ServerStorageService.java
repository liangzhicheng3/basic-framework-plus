package com.liangzhicheng.common.oss.service;

import com.liangzhicheng.common.exception.TransactionException;
import com.liangzhicheng.common.oss.properties.CloudStorage;
import com.liangzhicheng.common.utils.PrintUtil;
import org.apache.commons.io.IOUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.stream.FileImageOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * 服务器存储服务类
 * @author liangzhicheng
 */
public class ServerStorageService extends BaseCloudStorageService {

    public ServerStorageService(CloudStorage cloudStorage) {
        this.cloudStorage = cloudStorage;
    }

    @Override
    public String upload(MultipartFile file) throws Exception {
        String fileName = file.getOriginalFilename();
        String prefix = fileName.substring(fileName.lastIndexOf(".") + 1);
        return upload(file.getBytes(), String.format("%s.%s", super.getPath(""), prefix));
    }

    @Override
    public String upload(InputStream inputStream, String path) {
        try {
            byte[] data = IOUtils.toByteArray(inputStream);
            return upload(data, path);
        } catch (IOException e) {
            throw new TransactionException("上传文件失败，请检查服务器配置信息", e);
        }
    }

    @Override
    public String upload(byte[] data, String path) {
        if (data.length < 3 || path.equals("")) {
            throw new TransactionException("上传文件为空");
        }
        //本地存储必需要以"/"开头
        if (!path.startsWith("/")) {
            path = String.format("/%s", path);
        }
        try {
            String dateDir = path.split("/")[1];
            //文件夹
            File dirFile = new File(String.format("%s/%s", cloudStorage.getServerPath(), dateDir));
            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }
            File file = new File(String.format("%s%s", cloudStorage.getServerPath(), path));
            if (!file.exists()) {
                file.createNewFile();
            }
            FileImageOutputStream imageOutput = new FileImageOutputStream(file);//打开输入流
            imageOutput.write(data, 0, data.length);
            imageOutput.close();
        } catch (Exception e) {
            PrintUtil.error("[本地] 上传发生异常，异常信息为：{}", e.getMessage());
            throw new TransactionException("上传文件失败，请检查服务器配置信息", e);
        }
        return String.format("%s%s", cloudStorage.getServerProxy(), path);
    }

    @Override
    public String upload(String filePath, String fileName) {
        return null;
    }

}
