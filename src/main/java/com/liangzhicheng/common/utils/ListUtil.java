package com.liangzhicheng.common.utils;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;

import java.nio.charset.Charset;
import java.util.*;

/**
 * List工具类
 * @author liangzhicheng
 */
public class ListUtil {

    /**
     * 字符串以split为分割符，转换成List
     * @param value
     * @param split
     * @return List<String>
     */
    public static List<String> toList(String value, String split){
        if(ToolUtil.isBlank(value, split)){
            return null;
        }
        List<String> list = null;
        String[] array = value.split(split);
        if(array != null && array.length > 0){
            list = new ArrayList<String>();
            for(String str : array){
                if(!ToolUtil.isBlank(str)){
                    list.add(str);
                }
            }
        }
        return list;
    }

    /**
     * 从指定路径获取JSON并转换为List
     * @param path json文件路径
     * @param elementType List元素类型
     * @param <T> 泛型
     * @return List<T>
     */
    public static <T> List<T> toList(String path, Class<T> elementType) {
        ClassPathResource resource = new ClassPathResource(path);
        String jsonStr = IoUtil.read(resource.getStream(), Charset.forName("UTF-8"));
        return JSONUtil.toList(new JSONArray(jsonStr), elementType);
    }

    /**
     * 去除List中的重复值
     * @param oldList
     * @return List<String>
     */
    public static List<String> removeRepeat(List<String> oldList){
        List<String> list = null;
        if(oldList != null && oldList.size() > 0){
            list = new ArrayList<>(oldList.size());
            for(String str : oldList){
                if(!list.contains(str)){
                    list.add(str);
                }
            }
        }
        return list;
    }

    /**
     * 获取两个List的交集
     * @param firstList
     * @param secondList
     * @return List<String>
     */
    public static List<String> getListBoth(List<String> firstList, List<String> secondList) {
        List<String> resultList = new ArrayList<>();
        LinkedList<String> first = new LinkedList<>(firstList); //大集合用LinkedList
        HashSet<String> second = new HashSet<>(secondList); //小集合用HashSet
        Iterator<String> iterator = first.iterator(); //Iterator迭代器进行数据操作
        while(iterator.hasNext()) {
            if(!second.contains(iterator.next())) {
                iterator.remove();
            }
        }
        resultList = new ArrayList<>(first);
        return resultList;
    }

    /**
     * 下划线字符串字段转化成驼峰字段
     * @param list
     * @return List
     */
    public static List<Map<String, Object>> formatHump(List<Map<String, Object>> list){
        List<Map<String, Object>> resultList = new ArrayList<>(list.size());
        for(Map<String, Object> map : list){
            resultList.add(handle(map));
        }
        return resultList;
    }

    public static Map<String, Object> handle(Map<String, Object> map){
        Map<String, Object> newMap = new HashMap<>();
        Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry<String, Object> entry = it.next();
            String key = entry.getKey();
            String newKey = toHump(key);
            Object value = entry.getValue();
            if(value != null && value != ""){
                newMap.put(newKey, value);
            }else{
                newMap.put(newKey, "");
            }
        }
        return newMap;
    }

    public static String toHump(String colName){
        StringBuilder sb = new StringBuilder();
        String[] str = colName.toLowerCase().split("_");
        int i = 0;
        for(String s : str){
            if (s.length() == 1) {
                s = s.toUpperCase();
            }
            i++;
            if (i == 1){
                sb.append(s);
                continue;
            }
            if (s.length() > 0){
                sb.append(s.substring(0, 1).toUpperCase());
                sb.append(s.substring(1));
            }
        }
        return sb.toString();
    }

    /**
     * 判断集合是否为空并且数量大于0
     * @param collection
     * @return boolean
     */
    public static boolean sizeGT(Collection collection) {
        if (collection == null || collection.size() < 1) {
            return false;
        }
        return true;
    }

}
