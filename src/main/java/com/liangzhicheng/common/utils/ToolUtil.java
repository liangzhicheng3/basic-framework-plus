package com.liangzhicheng.common.utils;

import com.liangzhicheng.common.exception.TransactionException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.net.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 常用工具类
 * @author liangzhicheng
 */
public class ToolUtil {

    /**
     * 生成id
     * @param
     * @return
     */
    public static String generateId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 随机生成6位数的字符串
     * @param
     * @return String
     */
    public static String generateRandom(){
        int num = (int)((Math.random() * 9 + 1) * 100000);
        return String.valueOf(num);
    }

    /**
     * 根据num随机生成n位数的字符串
     * @param num
     * @return String
     */
    public static String generateRandomInteger(Integer num) {
        String base = "0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < num; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 根据num随机生成n位数的字符串
     * @param num
     * @return String
     */
    public static String generateRandomString(Integer num) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < num; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 生成用户昵称
     * @return String
     */
    public static String generateNickname() {
        String nickname = "";
        Random random = new Random();
        //参数length，表示生成几位随机数
        for (int i = 0; i < 8; i++) {
            String str = random.nextInt(2) % 2 == 0 ? "char" : "num";
            //输出字母还是数字
            if ("char".equalsIgnoreCase(str)) {
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                nickname += (char) (random.nextInt(26) + temp);
            } else if ("num".equalsIgnoreCase(str)) {
                nickname += String.valueOf(random.nextInt(10));
            }
        }
        return "用户_" + nickname;
    }

    /**
     * 随机生成一个16进制颜色
     * @return String
     */
    public static String generateColor(){
        String r, g, b;
        Random random = new Random();
        r = Integer.toHexString(random.nextInt(256)).toUpperCase();
        g = Integer.toHexString(random.nextInt(256)).toUpperCase();
        b = Integer.toHexString(random.nextInt(256)).toUpperCase();
        r = r.length() == 1 ? "0" + r : r ;
        g = g.length() == 1 ? "0" + g : g ;
        b = b.length() == 1 ? "0" + b : b ;
        String color = r + g + b;
        return color;
    }

    /**
     * 判断String参数是否为空，参数数量可变
     * @param strs
     * @return boolean
     */
    public static boolean isBlank(String ... strs){
        for(String s : strs){
            if(StringUtils.isBlank(s)){
                return true;
            }
        }
        return false;
    }

    /**
     * 判断多个参数是否为空，参数数量可变
     * @param strs
     * @return boolean
     */
    public static boolean isNotBlank(String ... strs){
        return !isBlank(strs);
    }

    /**
     * 判断对象或对象数组中每一个对象是否为空，对象为null，字符序列长度为0，集合类、Map类为empty
     * @param obj
     * @return boolean
     */
    public static boolean isNull(Object obj){
        if(obj == null){
            return true;
        }
        if(obj instanceof CharSequence){
            return ((CharSequence) obj).length() == 0;
        }
        if(obj instanceof Collection){
            return ((Collection) obj).isEmpty();
        }
        if(obj instanceof Map){
            return ((Map) obj).isEmpty();
        }
        if(obj instanceof Object[]){
            Object[] object = (Object[]) obj;
            if(object.length == 0){
                return true;
            }
            boolean empty = true;
            for(int i = 0; i < object.length; i++){
                if (!isNull(object[i])) {
                    empty = false;
                    break;
                }
            }
            return empty;
        }
        return false;
    }

    /**
     * 判断对象或对象数组中每一个对象是否为空，对象为null，字符序列长度为0，集合类、Map类为empty
     * @param obj
     * @return boolean
     */
    public static boolean isNotNull(Object obj){
        return !isNull(obj);
    }

    /**
     * 判断String参数是否为数字字符
     * @param str
     * @return boolean
     */
    public static boolean isNumber(String str){
        if(isBlank(str)){
            return false;
        }
        if(StringUtils.isNumeric(str)){
            return true;
        }
        return false;
    }

    /**
     * 判断String参数是否为数字字符，参数数量可变
     * @param strs
     * @return boolean
     */
    public static boolean isNumber(String ... strs){
        for(String s : strs){
            if(!isNumber(s)){
                return false;
            }
        }
        return true;
    }

    /**
     * 判断String参数是否为小数
     * @param str
     * @return boolean
     */
    public static boolean isDouble(String str){
        if(isBlank(str)){
            return false;
        }
        Pattern pattern = Pattern.compile("\\d+\\.\\d+$|-\\d+\\.\\d+$");
        Matcher m = pattern.matcher(str);
        return m.matches();
    }

    /**
     * 判断String参数是否为小数，参数数量可变
     * @param strs
     * @return boolean
     */
    public static boolean isDouble(String ... strs){
        for(String s : strs){
            if(!isDouble(s)){
                return false;
            }
        }
        return true;
    }

    /**
     * 判断String参数是否是一个正确的手机号码
     * @param phone
     * @return boolean
     */
    public static boolean isPhone(String phone) {
        if(isBlank(phone) || phone.length() != 11){
            return false;
        }
        Pattern p = Pattern.compile("^((13[0-9])|(14[0-9])|(17[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
        Matcher m = p.matcher(phone);
        return m.matches();
    }

    /**
     * 判断是否邮箱格式
     * @param email
     * @return boolean
     */
    public static boolean isEmail(String email) {
        Pattern emailPattern = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
        Matcher matcher = emailPattern.matcher(email);
        if (matcher.find()) {
            return true;
        }
        return false;
    }

    /**
     * 判断String参数是否中文，是返回true
     * @param str
     * @return boolean
     */
    public static boolean isChinese(String str) {
        if(isNotBlank(str)){
            char[] arr = str.toCharArray();
            if(arr != null && arr.length > 0){
                for(char c : arr){
                    Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
                    if (ub != Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                            && ub != Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                            && ub != Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                            && ub != Character.UnicodeBlock.GENERAL_PUNCTUATION
                            && ub != Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                            && ub != Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 判断文件是否图片
     * @param suffix
     * @return boolean
     */
    public static boolean isImage(String suffix) {
        return imageHandle(suffix);
    }

    /**
     * 判断文件是否图片
     * @param file
     * @return boolean
     */
    public static boolean isImage(MultipartFile file) {
        String suffix = getFileSuffix(file);
        return imageHandle(suffix);
    }

    /**
     * 判断文件是否视频
     * @param suffix
     * @return boolean
     */
    public static boolean isVideo(String suffix) {
        return videoHandle(suffix);
    }


    /**
     * 判断文件是否视频
     * @param file
     * @return boolean
     */
    public static boolean isVideo(MultipartFile file) {
        String suffix = getFileSuffix(file);
        return videoHandle(suffix);
    }

    /**
     * 获取文件后缀，包含.
     * @param file
     * @return String
     */
    public static String getFileSuffix(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        if(!fileName.contains(".")){
            return null;
        }
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        return suffix;
    }

    /**
     * 判断String参数是否存在可变参数中，如果不存在返回true
     * @param str
     * @param strs
     * @return boolean
     */
    public static boolean notIn(String str, String ... strs){
        for(String s : strs){
            if(s.equals(str)){
                return false;
            }
        }
        return true;
    }

    /**
     * 判断String参数是否存在可变参数中，如果存在返回true
     * @param str
     * @param strs
     * @return boolean
     */
    public static boolean in(String str, String ... strs){
        for(String s : strs){
            if(s.equals(str)){
                return true;
            }
        }
        return false;
    }

    /**
     * 判断String可变参数中是否存在，存在返回true
     * @param strs
     * @return boolean
     */
    public static boolean inOneByNotBlank(String ... strs) {
        for (String s : strs) {
            if(isNotBlank(s)){
                return true;
            }
        }
        return false;
    }

    /**
     * 返回将number补0，长度为length位后的字符
     * @param number 要补0的数字
     * @param length 补0后的长度
     * @return String
     */
    public static String toLength(int number, int length){
        return String.format("%0" + length + "d", number);
    }

    /**
     * 判断字符串长度，中文为2，字母为1
     * @param value
     * @return int
     */
    public static int strLength(String value) {
        int valueLength = 0;
        String chinese = "[\u4e00-\u9fa5]";
        for (int i = 0; i < value.length(); i++) {
            String temp = value.substring(i, i + 1);
            if (temp.matches(chinese)) {
                valueLength += 2;
            } else {
                valueLength += 1;
            }
        }
        return valueLength;
    }

    /**
     * 将字符串中的emoji表情转换成可以在utf-8字符集数据库中保存的格式(表情占4个字节，需要utf8mb4字符集)
     * @param str
     * @return String
     */
    public static String emojiConvert(String str){
        if(isNotBlank(str)){
            try{
                String patternString = "([\\x{10000}-\\x{10ffff}\ud800-\udfff])";
                Pattern pattern = Pattern.compile(patternString);
                Matcher matcher = pattern.matcher(str);
                StringBuffer sb = new StringBuffer();
                while(matcher.find()) {
                    matcher.appendReplacement(sb,"[[" + URLEncoder.encode(matcher.group(1),"UTF-8") + "]]");
                }
                matcher.appendTail(sb);
                return sb.toString();
            }catch(Exception e){
                PrintUtil.error("表情转换有误：{}", e.getMessage());
                throw new TransactionException("表情转换有误");
            }
        }
        return null;
    }

    /**
     * 还原utf-8字符集数据库中保存的含转换后emoji表情的字符串
     * @param str
     * @return String
     */
    public static String emojiRecovery(String str) {
        if(isNotBlank(str)){
            try{
                if(str == null){
                    str = "";
                }
                String patternString = "\\[\\[(.*?)\\]\\]";
                Pattern pattern = Pattern.compile(patternString);
                Matcher matcher = pattern.matcher(str);
                StringBuffer sb = new StringBuffer();
                while(matcher.find()) {
                    matcher.appendReplacement(sb, URLDecoder.decode(matcher.group(1), "UTF-8"));
                }
                matcher.appendTail(sb);
                return sb.toString();
            }catch(Exception e){
                PrintUtil.error("表情转换有误：{}", e.getMessage());
                throw new TransactionException("表情转换有误");
            }
        }
        return null;
    }

    /**
     * 判断文件是否图片公用方法
     * @param suffix
     * @return boolean
     */
    private static boolean imageHandle(String suffix){
        boolean ok = false;
        if(isNotBlank(suffix)) {
            String[] arr = {".png", ".jpg", ".jpeg", ".gif", ".bmp"};
            for(String s : arr) {
                if(suffix.toLowerCase().equals(s)) {
                    ok = true;
                    return ok;
                }
            }
        }
        return ok;
    }

    /**
     * 判断文件是否视频公用方法
     * @param suffix
     * @return boolean
     */
    private static boolean videoHandle(String suffix){
        boolean ok = false;
        if(isNotBlank(suffix)) {
            String[] arr = {".flv", ".swf", ".mkv", ".avi", ".rm", ".rmvb", ".mpeg", ".mpg",".ogg", ".ogv", ".mov", ".wmv", ".mp4", ".webm", ".mp3", ".wav", ".mid"};
            for(String s : arr) {
                if(suffix.toLowerCase().equals(s)) {
                    ok = true;
                    return ok;
                }
            }
        }
        return ok;
    }

}
