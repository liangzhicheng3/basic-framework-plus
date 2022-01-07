package com.liangzhicheng.common.utils;

import com.liangzhicheng.common.exception.TransactionException;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Xml工具类
 * @author liangzhicheng
 */
public class XmlUtil {

    /**
     * 将String的Xml格式转化成Map
     * @param xml
     * @return Map<String,String>
     */
    public static Map<String, String> toMap(String xml){
        try{
            Map<String, String> map = new HashMap<>();
            Document doc = DocumentHelper.parseText(xml);
            Element root = doc.getRootElement();
            List<Element> list = root.elements();
            for(Element e : list){
                map.put(e.getName(), e.getText());
            }
            return map;
        }catch(DocumentException e){
            PrintUtil.error("String的Xml格式转化成Map有误：{}", e.getMessage());
            throw new TransactionException("String的Xml格式转化成Map有误");
        }
    }

    /**
     * 将Map转化成Xml格式
     * @param map
     * @return String
     */
    public static String toXml(Map map){
        if(map != null){
            Element root = DocumentHelper.createElement("xml");
            Document document = DocumentHelper.createDocument(root);
            Set<String> set = map.keySet();
            for(String key : set){
                if(map.get(key) != null){
                    root.addElement(key).addText(String.valueOf(map.get(key)));
                }
            }
            return document.asXML();
        }
        return "";
    }

}
