package com.liangzhicheng.common.utils;

import cn.hutool.core.io.FileUtil;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * 文件上传工具类（上传至本地服务器）
 * @author liangzhicheng
 */
public class FilesUtil {

    /**
     * 文件上传
     * @param file
     * @param dir 默认/upload
     * @return String
     */
    public static String upload(MultipartFile file, String dir){
        try {
            if (ToolUtil.isBlank(dir)) {
                dir = "/upload";
            }
            String saveDir = getProjectPath() + dir;
            String newFileName = file.getOriginalFilename();
            String suffix = "";
            if (file.getOriginalFilename().lastIndexOf(".") != -1) {
                newFileName = file.getOriginalFilename().substring(0, file.getOriginalFilename().lastIndexOf("."));
                suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            }
            while (FileUtil.exist(String.format("%s/%s%s", saveDir, newFileName, suffix))) {
                newFileName = newFileName + "-1";
            }
            File parent = new File(saveDir);
            parent.mkdirs();
            File newFile = new File(saveDir, String.format("%s%s", newFileName, suffix));
            newFile.createNewFile();
            file.transferTo(newFile);
            return String.format("%s/%s%s", dir, newFileName, suffix);
        } catch (IOException e) {
            PrintUtil.error("[文件上传] 发生异常，异常信息为：{}", e.getMessage());
            return null;
        }
    }

    /**
     * 获取项目绝对路径(如：/opt/aa/bb.jar；返回：/opt/aa)
     * @return String
     */
    public static String getProjectPath(){
        String jarPath = FileUtil.getAbsolutePath("").replace("!/BOOT-INF/classes!/","");
        String dir = jarPath.substring(0,jarPath.lastIndexOf("/"));
//        String path = "/usr/workspace/project/";
//        String dir = path.substring(0, path.lastIndexOf("/"));
        return dir;
    }

}
