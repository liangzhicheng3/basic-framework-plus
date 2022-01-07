package com.liangzhicheng.common.utils;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.LinkedHashMap;

/**
 * 获取配置信息工具类
 * @author liangzhicheng
 */
public class ResourceUtil {

    public static String getValue(String key) {
        Yaml yaml = new Yaml();
        InputStream in = ResourceUtil.class.getResourceAsStream("/application.yml");
        LinkedHashMap<String, Object> sourceMap = (LinkedHashMap<String, Object>) yaml.load(in);
        String[] keys = key.split("[.]");
        LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) sourceMap.clone();
        int length = keys.length;
        Object resultValue = null;
        for (int i = 0; i < length; i++) {
            Object value = map.get(keys[i]);
            if (i < length - 1) {
                map = ((LinkedHashMap<String, Object>) value);
            } else if (value == null) {
                throw new RuntimeException("key is not exists");
            } else {
                resultValue = value;
            }
        }
        return resultValue.toString();
    }

}
