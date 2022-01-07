package com.liangzhicheng.common.pay.wechat.utils;

import com.liangzhicheng.common.utils.PrintUtil;
import com.liangzhicheng.common.utils.ToolUtil;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.*;

public class XmlUtil {

    /**
     * 将map对象转换成xml格式字符串
     * @param params map对象
     * @return String
     */
    public static String mapToXmlStr(Map<Object, Object> params) {
        StringBuffer xmlStr = new StringBuffer();
        if(ToolUtil.isNotNull(params)){
            xmlStr.append("<xml>");
            Set<Object> keySet = params.keySet();
            Iterator<Object> keyIte = keySet.iterator();
            while (keyIte.hasNext()) {
                String key = (String) keyIte.next();
                String val = String.valueOf(params.get(key));
                xmlStr.append("<");
                xmlStr.append(key);
                xmlStr.append(">");
                xmlStr.append(val);
                xmlStr.append("</");
                xmlStr.append(key);
                xmlStr.append(">");
            }
            xmlStr.append("</xml>");
        }
        return xmlStr.toString();
    }

    /**
     * 将xml格式字符串转换成map对象
     * @param xmlStr xml格式字符串
     * @return Map对象
     * @throws Exception
     */
    public static Map<String, Object> xmlStrToMap(String xmlStr) throws Exception {
        if(ToolUtil.isBlank(xmlStr)){
            return null;
        }
        Map<String, Object> map = new HashMap<>();
        //将xml格式的字符串转换成Document对象
        Document doc = DocumentHelper.parseText(xmlStr);
        //获取根节点
        Element root = doc.getRootElement();
        //获取根节点下的所有元素
        List children = root.elements();
        //循环所有子元素
        if(children != null && children.size() > 0){
            for(int i = 0; i < children.size(); i++){
                Element child = (Element) children.get(i);
                map.put(child.getName(), child.getTextTrim());
            }
        }
        return map;
    }

    /**
     * 将xml格式字符串转换成bean对象
     * @param xmlStr xml格式字符串
     * @param clazz 待转换的class
     * @return 转换后对象
     */
    public static Object xmlStrToBean(String xmlStr, Class<?> clazz) {
        Object obj = null;
        try {
            // 将xml格式的数据转换成Map对象
            Map<String, Object> map = xmlStrToMap(xmlStr);
            // 将map对象的数据转换成Bean对象
            obj = mapToBean(map, clazz);
        } catch (Exception e) {
            PrintUtil.error("[xmlStrToBean] 转换异常：{}", e.getMessage());
        }
        return obj;
    }

    /**
     * 将流转换成xml格式字符串
     * @param request
     * @param response
     * @return String
     */
    public static String inputStreamToXmlStr(HttpServletRequest request, HttpServletResponse response){
        ByteArrayOutputStream out = null;
        InputStream in = null;
        String xml = null;
        try {
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
            response.setHeader("Access-Control-Allow-Origin", "*");
            out = new ByteArrayOutputStream();
            in = request.getInputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            xml = new String(out.toByteArray(), "UTF-8");
        } catch (Exception e) {
            PrintUtil.error("[inputStreamToXmlStr] 转换异常：{}", e.getMessage());
        } finally {
            try {
                if(out != null){
                    out.close();
                }
                if(in != null){
                    in.close();
                }
            } catch (IOException e) {
                PrintUtil.error("[inputStreamToXmlStr] 输出异常：{}", e.getMessage());
            }
        }
        return xml;
    }

    /**
     * 将map对象通过反射机制转换成bean对象
     * @param map map对象
     * @param clazz 待转换的class
     * @return 转换后Bean对象
     * @throws Exception
     */
    private static Object mapToBean(Map<String, Object> map, Class<?> clazz) throws Exception {
        Object obj = clazz.newInstance();
        if (map != null && map.size() > 0) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String propertyName = entry.getKey();
                Object value = entry.getValue();
                String setMethodName = "set" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
                Field field = getClassField(clazz, propertyName);
                if (field != null) {
                    Class<?> fieldTypeClass = field.getType();
                    value = convertValType(value, fieldTypeClass);
                    clazz.getMethod(setMethodName, field.getType()).invoke(obj, value);
                }
            }
        }
        return obj;
    }

    /**
     * 获取指定字段名称查找在class中的对应的Field对象（包括查找父类）
     * @param clazz 指定的class
     * @param fieldName 字段名称
     * @return Field对象
     */
    private static Field getClassField(Class<?> clazz, String fieldName) {
        if (Object.class.getName().equals(clazz.getName())) {
            return null;
        }
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            if (field.getName().equals(fieldName)) {
                return field;
            }
        }
        Class<?> superClass = clazz.getSuperclass();
        if (superClass != null) {// 简单的递归一下
            return getClassField(superClass, fieldName);
        }
        return null;
    }

    /**
     * 将Object类型的值，转换成bean对象属性里对应的类型值
     * @param value Object对象值
     * @param fieldTypeClass 属性的类型
     * @return 转换后对象
     */
    private static Object convertValType(Object value, Class<?> fieldTypeClass) {
        Object retVal = null;
        if (Long.class.getName().equals(fieldTypeClass.getName())
                || long.class.getName().equals(fieldTypeClass.getName())) {
            retVal = Long.parseLong(value.toString());
        } else if (Integer.class.getName().equals(fieldTypeClass.getName())
                || int.class.getName().equals(fieldTypeClass.getName())) {
            retVal = Integer.parseInt(value.toString());
        } else if (Float.class.getName().equals(fieldTypeClass.getName())
                || float.class.getName().equals(fieldTypeClass.getName())) {
            retVal = Float.parseFloat(value.toString());
        } else if (Double.class.getName().equals(fieldTypeClass.getName())
                || double.class.getName().equals(fieldTypeClass.getName())) {
            retVal = Double.parseDouble(value.toString());
        } else {
            retVal = value;
        }
        return retVal;
    }

}
