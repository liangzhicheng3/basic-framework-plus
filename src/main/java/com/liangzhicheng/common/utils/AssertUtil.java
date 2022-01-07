package com.liangzhicheng.common.utils;

import com.liangzhicheng.common.exception.TransactionException;

/**
 * 断言工具类
 * @author liangzhicheng
 */
public class AssertUtil {

    /**
     * 判断为false，true抛异常
     * @param result
     * @param code
     */
    public static void isFalse(boolean result, int code){
        isTrue(!result, code);
    }

    /**
     * 判断为false，true抛异常
     * @param result
     * @param message
     */
    public static void isFalse(boolean result, String message){
        isTrue(!result, message);
    }

    /**
     * 判断为true，false抛异常
     * @param result
     * @param code
     */
    public static void isTrue(boolean result, int code){
        if(!result){
            throw new TransactionException(code);
        }
    }

    /**
     * 判断为true，false抛异常
     * @param result
     * @param message
     */
    public static void isTrue(boolean result, String message){
        if(!result){
            throw new TransactionException(message);
        }
    }

}
