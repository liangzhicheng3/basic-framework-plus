package com.liangzhicheng.common.utils;

import com.liangzhicheng.common.constant.Constants;
import com.liangzhicheng.common.io.IOStreamReader;

import java.io.File;

/**
 * PDF工具类
 * @author liangzhicheng
 */
public class PDFUtil {

    /**
     * html转pdf
     * @param srcPath  html路径，可以是硬盘上的路径，也可以是网络路径
     * @param targetPath pdf保存路径
     * @return boolean
     */
    public static boolean convert(String srcPath, String targetPath) {
        File file = new File(targetPath);
        File parent = file.getParentFile();
        //如果pdf保存路径不存在，则创建路径
        if (!parent.exists()){
            parent.mkdirs();
        }
        StringBuilder cmd = new StringBuilder();
        cmd.append(Constants.PDF_UTIL_PATH); //wkhtmltopdf在系统中的路径
        cmd.append(" ");
        cmd.append(srcPath);
        cmd.append(" ");
        cmd.append(targetPath);
        boolean result = true;
        try {
            Process proc = Runtime.getRuntime().exec(cmd.toString());
            IOStreamReader error = new IOStreamReader(proc.getErrorStream());
            IOStreamReader output = new IOStreamReader(proc.getInputStream());
            error.start();
            output.start();
            proc.waitFor();
        } catch (Exception e) {
            result = false;
            PrintUtil.error("HTML转换成PDF有误：{}", e.getMessage());
        }
        return result;
    }

    /*public static void main(String[] args) {
        convert("http://www.baidu.com/", "D:\\data\\pdf\\jstarseven.pdf");
    }*/

}

