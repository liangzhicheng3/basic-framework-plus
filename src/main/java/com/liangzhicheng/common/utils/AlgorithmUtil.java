package com.liangzhicheng.common.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 算法工具类
 * @author liangzhicheng
 */
public class AlgorithmUtil {

    /**
     * 向上取整
     * @param num
     * @param size
     * @return int
     */
    public static int upCeil(int num, int size) {
        if (size == 0) {
            return 0;
        }
        int n1 = num / size;
        double n2 = num % size;
        if (n2 > 0) {
            n1 += 1;
        }
        return n1;
    }

    /**
     * 向下取整
     * @param num
     * @param size
     * @return int
     */
    public static int downCeil(int num, int size){
        if(size == 0){
            return 0;
        }
        int n1 = num / size;
        Math.floor(n1);
        return n1;
    }

    /**
     * 精确计算两个Double类型数相加，v1加v2
     * @param v1
     * @param v2
     * @return Double
     */
    public static Double add(Double v1, Double v2){
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.add(b2).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 精确计算两个BigDecimal类型数相加，v1加v2
     * @param v1
     * @param v2
     * @return BigDecimal
     */
    public static BigDecimal add(BigDecimal v1, BigDecimal v2){
        return v1.add(v2).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 精确计算两个Double类型数相减，v1减v2
     * @param v1
     * @param v2
     * @return Double
     */
    public static Double sub(Double v1, Double v2){
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.subtract(b2).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 精确计算两个BigDecimal类型数相减，v1减v2
     * @param v1
     * @param v2
     * @return BigDecimal
     */
    public static BigDecimal sub(BigDecimal v1, BigDecimal v2){
        return v1.subtract(v2).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 精确计算两个Double类型数相乘，v1乘以v2
     * @param v1
     * @param v2
     * @return Double
     */
    public static Double multiply(Double v1, Double v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.multiply(b2).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 精确计算两个BigDecimal类型数相乘，v1乘以v2
     * @param b1
     * @param b2
     * @return BigDecimal
     */
    public static BigDecimal multiply(BigDecimal b1, BigDecimal b2) {
        return b1.multiply(b2).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 精确计算两个Double类型数相除，v1除以v2
     * @param v1
     * @param v2
     * @return Double
     */
    public static Double divide(Double v1, Double v2) {
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        return b1.divide(b2, 10, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 精确计算两个BigDecimal类型数相除，v1除以v2
     * @param v1
     * @param v2
     * @return BigDecimal
     */
    public static BigDecimal divide(BigDecimal v1, BigDecimal v2){
        return v1.divide(v2, 10, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 将转化得到的String不是科学计数法，如：4.3062319920812602E17->43062319920812602.00
     * @param value
     * @param pattern
     * @return String
     */
    public static String toString(Double value, String pattern){
        if(ToolUtil.isBlank(pattern)){
            pattern = "0.00";
        }
        DecimalFormat df = new DecimalFormat(pattern);
        return df.format(value);
    }

    /**
     * Double金额转String字符串，如超过万转成1.00万
     * @param value
     * @return String
     */
    public static String toString(Double value) {
        String str = "";
        if (value >= 10000 && value < 1000000) {
            value = value * 0.0001;
            DecimalFormat df = new DecimalFormat("######0.0");
            str = df.format(value);
            str += "万";
        } else if (value >= 1000000 && value < 10000000) {
            value = value * 0.000001;
            DecimalFormat df = new DecimalFormat("######0.0");
            str = df.format(value);
            str += "百万";
        } else if (value >= 10000000) {
            value = value * 0.0000001;
            DecimalFormat df = new DecimalFormat("######0.0");
            str = df.format(value);
            str += "千万";
        } else {
            str = Double.toString(value);
        }
        return str;
    }

}
