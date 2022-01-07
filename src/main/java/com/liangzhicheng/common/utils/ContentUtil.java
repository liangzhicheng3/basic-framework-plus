package com.liangzhicheng.common.utils;

import com.liangzhicheng.common.constant.Constants;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * 敏感词过滤工具类（利用DFA算法进行敏感词过滤）
 * @author liangzhicheng
 */
public class ContentUtil {

    /**
     * 最小匹配规则
     */
    public static int minMatchType = 1;
    /**
     * 最大匹配规则
     */
    public static int maxMatchType = 2;

    /**
     * 替换敏感字字符
     * @param content
     * @param matchType
     * @param replaceStr
     * @return String
     */
    public static String filter(String content, int matchType, String replaceStr) {
        String result = content;
        //获取所有的敏感词
        Set<String> set = getWord(content, matchType);
        Iterator<String> iterator = set.iterator();
        String word = null;
        String replaceString = null;
        while (iterator.hasNext()) {
            word = iterator.next();
            replaceString = getReplaceStr(replaceStr, word.length());
            result = result.replaceAll(word, replaceString);
        }
        return result;
    }

    /**
     * 获取文字中的敏感词
     * @param content
     * @param matchType
     * @return Set<String>
     */
    public static Set<String> getWord(String content, int matchType) {
        Set<String> wordList = new HashSet<>();
        for (int i = 0; i < content.length(); i++) {
            //判断是否包含敏感字符
            int length = validateWord(content, i, matchType);
            //存在加入list中
            if (length > 0) {
                wordList.add(content.substring(i, i + length));
                //减1的原因，是因为for会自增
                i = i + length - 1;
            }
        }
        return wordList;
    }

    /**
     * 检查文字中是否包含敏感字符，检查规则如下：如果存在，则返回敏感词字符的长度，不存在返回0
     * @param content
     * @param beginIndex
     * @param matchType
     * @return int
     */
    public static int validateWord(String content, int beginIndex, int matchType) {
        //敏感词结束标识位，用于敏感词只有1位的情况
        boolean flag = false;
        //匹配标识数默认为0
        int matchFlag = 0;
        Map nowMap = init();
        for (int i = beginIndex; i < content.length(); i++) {
            char word = content.charAt(i);
            //获取指定key
            nowMap = (Map) nowMap.get(word);
            //存在，则判断是否为最后一个
            if (nowMap != null) {
                //找到相应key，匹配标识+1
                matchFlag++;
                //如果为最后一个匹配规则，结束循环，返回匹配标识数
                if ("1".equals(nowMap.get("isEnd"))) {
                    //结束标志位为true
                    flag = true;
                    //最小规则，直接返回，最大规则还需继续查找
                    if (ContentUtil.minMatchType == matchType) {
                        break;
                    }
                }
            } else { //不存在，直接返回
                break;
            }
        }
        if (ContentUtil.maxMatchType == matchType) {
            if (matchFlag < 2 || !flag) { //长度必须大于等于1，为词
                matchFlag = 0;
            }
        }
        if (ContentUtil.minMatchType == matchType) {
            if (matchFlag < 2 && !flag) { //长度必须大于等于1，为词
                matchFlag = 0;
            }
        }
        return matchFlag;
    }

    /**
     * 初始化敏感字库
     * @return Map
     */
    public static Map init() {
        //读取敏感词库，存入Set中
        Set<String> words = readWord();
        //将敏感词库加入到HashMap中，确定有穷自动机DFA
        return addWord(words);
    }

    /**
     * 读取敏感词库，存入HashMap中
     * @return Set<String>
     */
    private static Set<String> readWord() {
        Set<String> wordSet = null;
        //敏感词库
        File file = new File(Constants.CONTENT_WORDS_PATH);
        try {
            //读取文件输入流
            InputStreamReader read = new InputStreamReader(new FileInputStream(file), "UTF-8");
            //文件是否是文件和是否存在
            if (file.isFile() && file.exists()) {
                wordSet = new HashSet<String>();
                //BufferedReader是包装类，先把字符读到缓存里，到缓存满了，再读入内存，提高了读效率
                BufferedReader br = new BufferedReader(read);
                String txt = null;
                //读取文件，将文件内容放入到set中
                while ((txt = br.readLine()) != null) {
                    wordSet.add(txt);
                }
                br.close();
            }
            //关闭文件流
            read.close();
        } catch (Exception e) {
            PrintUtil.error("读取敏感词库，存入HashMap中有误：{}", e.getMessage());
        }
        return wordSet;
    }

    /**
     * 将HashSet中的敏感词，存入HashMap中
     * @param words
     * @return Map
     */
    private static Map addWord(Set<String> words) {
        //初始化敏感词容器，减少扩容操作
        Map wordMap = new HashMap(words.size());
        for (String word : words) {
            Map nowMap = wordMap;
            for (int i = 0; i < word.length(); i++) {
                //转换成char型
                char keyChar = word.charAt(i);
                //获取
                Object tempMap = nowMap.get(keyChar);
                //如果存在该key，直接赋值
                if (tempMap != null) {
                    nowMap = (Map) tempMap;
                } else { //不存在则，则构建一个map，同时将isEnd设置为0
                    //设置标志位
                    Map<String, String> newMap = new HashMap<String, String>();
                    newMap.put("isEnd", "0");
                    //添加到集合
                    nowMap.put(keyChar, newMap);
                    nowMap = newMap;
                }
                //最后一个
                if (i == word.length() - 1) {
                    nowMap.put("isEnd", "1");
                }
            }
        }
        return wordMap;
    }

    /**
     * 获取替换字符串
     * @param replaceStr
     * @param length
     * @return String
     */
    private static String getReplaceStr(String replaceStr, int length) {
        String result = replaceStr;
        for (int i = 1; i < length; i++) {
            result += replaceStr;
        }
        return result;
    }

//    public static void main(String[] args) {
//        String content = "这条信息太过激情啦";
//        String filter = filter(content, 2, "*");
//        System.out.println(filter);
//    }

}
