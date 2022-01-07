package com.liangzhicheng.common.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ReflectUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Bean工具类
 * @author liangzhicheng
 */
public class BeansUtil {

    /**
     * 拷贝单个entity
     * @param source
     * @param cls
     * @param <T>
     * @return T
     */
    public static<T> T copyEntity(Object source, Class<T> cls){
        T target = null;
        if(source != null){
            target = ReflectUtil.newInstance(cls);
            BeanUtil.copyProperties(source, target);
        }
        return target;
    }

    /**
     * 拷贝整个list，由于hutool只有单个bean的拷贝，没有整个List的拷贝，需要封装一个list的拷贝
     * @param source
     * @param cls
     * @param <T>
     * @return List<T>
     */
    public static<T> List<T> copyList(List<?> source, Class<T> cls) {
        if(source == null || source.size() == 0){
            return new ArrayList<>();
        }
        List<T> targetList = new ArrayList<>(source.size());
        T target = null;
        for (Object obj : source) {
            if(obj != null){
                target = ReflectUtil.newInstance(cls);
                BeanUtil.copyProperties(obj, target);
            }
            targetList.add(target);
        }
        return targetList;
    }

}
