package com.liangzhicheng.common.io;

import com.liangzhicheng.common.utils.PrintUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 输入流，输出流（继承Thread，实现开启线程）
 * @author liangzhicheng
 */
public class IOStreamReader extends Thread {

    private InputStream is;

    public IOStreamReader(InputStream is) {
        this.is = is;
    }

    @Override
    public void run() {
        try {
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                PrintUtil.info("[IOStreamReader] 调用，输出信息为：{}", line); //输出内容
            }
        } catch (IOException e) {
            PrintUtil.error("IOStreamReader 输出有误：{}", e.getMessage());
        }
    }

}
