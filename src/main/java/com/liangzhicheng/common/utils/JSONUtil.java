package com.liangzhicheng.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.multipart.MultipartFile;

/**
 * JSO序列化工具类
 * @author liangzhicheng
 */
public class JSONUtil {

    private static final ObjectMapper DEFAULT_OBJECT_MAPPER = new ObjectMapper();

    public static String toJSONString(Object obj) {
        try {
            if (obj instanceof MultipartFile) {
                return "文件";
            }
            return DEFAULT_OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            PrintUtil.error("[JSON序列化] 序列化 JSON 失败", e);
            return "";
        }
    }

    public static <T> T parseObject(String json, Class<T> clazz) {
        if (ToolUtil.isBlank(json)) {
            return null;
        }
        try {
            return DEFAULT_OBJECT_MAPPER.readValue(json, clazz);
        } catch (Exception e) {
            PrintUtil.error("[JSON序列化] 反序列化 JSON 失败", e);
            return null;
        }
    }

}
