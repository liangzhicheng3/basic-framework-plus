package com.liangzhicheng.common.utils;

import com.liangzhicheng.common.bean.RedisBean;
import com.liangzhicheng.config.context.SpringContextHolder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 创建定长线程池，可控制线程最大并发数，超出的线程会在队列中等待
 * @author liangzhicheng
 */
public class ThreadUtil {

    private RedisBean redisBean = SpringContextHolder.getBean(RedisBean.class);

    private ExecutorService fixedThreadPool = Executors.newFixedThreadPool(2);

    private ThreadUtil() {}

    private static ThreadUtil instance;

    public static ThreadUtil getInstance() {
        if(instance == null){
            instance = new ThreadUtil();
        }
        return instance;
    }

    public void refreshPermMenu(){
        fixedThreadPool.submit(new Runnable() {
            @Override
            public void run() {
                redisBean.refreshPermMenu();
            }
        });
    }

    public void refreshRolePerm(){
        fixedThreadPool.submit(new Runnable() {
            @Override
            public void run() {
                redisBean.refreshRolePerm();
            }
        });
    }

}
