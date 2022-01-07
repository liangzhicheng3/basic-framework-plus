package com.liangzhicheng.common.utils;

import com.liangzhicheng.common.exception.TransactionException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 订单号生成工具类
 * @author liangzhicheng
 */
public class OrderNoUtil {

    //使用单例模式，不允许直接创建实例
    private OrderNoUtil(){}
    //创建一个空实例对象，类需要用的时候才赋值
    private static OrderNoUtil instance = null;
    //单例模式
    public static synchronized OrderNoUtil getInstance(){
        if (instance == null){
            instance = new OrderNoUtil();
        }
        return instance;
    }

    //定义锁对象
    private static final ReentrantLock lock = new ReentrantLock();
    //格式化时间字符串
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    //记录上一次时间，用来判断是否需要递增全局数
    private static String now = null;
    //全局自增数
    private static int count = 1;

    //调用的方法
    public static String generateOrderNo(){
        String orderNo = null;
        String dateStr = getCurrentDateStr();
        lock.lock(); //加锁
        if(dateStr.equals(now)){ //判断是时间是否相同
            orderNo = orderNoDispose(orderNo);
        }else{
            now = getCurrentDateStr();
            orderNo = orderNoDispose(orderNo);
        }
        return orderNo;
    }

    //订单号处理
    private static String orderNoDispose(String orderNo){
        String randomNum = ToolUtil.generateRandom();
        try {
            if(count >= 10000){
                count = 1;
            }
            if(count < 10){
                orderNo = getCurrentDateStr() + "000" + count + randomNum;
            }else if(count < 100){
                orderNo = getCurrentDateStr() + "00" + count + randomNum;
            }else if(count < 1000){
                orderNo = getCurrentDateStr() + "0" + count + randomNum;
            }else{
                orderNo = getCurrentDateStr() + count + randomNum;
            }
            count++;
        }catch(Exception e){
            PrintUtil.error("[生成订单号] 发生异常，异常信息为：{}", e.getMessage());
            throw new TransactionException("生成订单号发生未知错误");
        }finally{
            lock.unlock();
        }
        return orderNo;
    }

    //获取当前时间年月日时分秒毫秒字符串
    private static String getCurrentDateStr(){
        return sdf.format(new Date());
    }

}
