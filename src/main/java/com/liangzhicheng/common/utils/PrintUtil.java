package com.liangzhicheng.common.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 日志打印工具类
 * @author liangzhicheng
 */
public class PrintUtil {

    private static final Logger logger = LogManager.getLogger(PrintUtil.class);

    public static void info(String message, Object ... object){
        logger.info(message, object);
    }

    public static void warn(String message, Object ... object){
        logger.warn(message, object);
    }

    public static void error(String message, Object ... object){
        logger.error(message, object);
    }

}
