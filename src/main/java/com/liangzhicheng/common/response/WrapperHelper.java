package com.liangzhicheng.common.response;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.liangzhicheng.common.exception.TransactionException;
import com.liangzhicheng.common.response.annotation.Query;
import com.liangzhicheng.common.utils.PrintUtil;
import com.liangzhicheng.common.utils.TimeUtil;
import com.liangzhicheng.common.utils.ToolUtil;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Mybatis Plus条件构建助手
 * @author liangzhicheng
 */
public class WrapperHelper {

    private WrapperHelper() {}

    private static WrapperHelper instance;

    public static WrapperHelper getInstance() {
        if(instance == null){
            instance = new WrapperHelper();
        }
        return instance;
    }

    /**
     * 获取查询条件
     * @param entity 实体类
     * @param query 查询对象
     * @param <E> 对应实体类类型
     * @param <Q> 对应查询对象类型
     * @return QueryWrapper
     */
    public <E, Q> QueryWrapper buildCondition(E entity, Q query) {
        QueryWrapper<E> queryWrapper = new QueryWrapper<>();
        if (ToolUtil.isNull(query)) {
            return queryWrapper;
        }
        try {
            List<Field> fields = getAllFields(query.getClass(), new ArrayList<>());
            for (Field field : fields) {
                boolean accessible = field.isAccessible();
                field.setAccessible(true);
                Query queryAnno = field.getAnnotation(Query.class);
                if (ToolUtil.isNotNull(queryAnno)) {
                    String propName = queryAnno.propName();
                    String blurry = queryAnno.blurry();
                    String attributeName = ToolUtil.isBlank(propName) ? field.getName() : propName;
                    attributeName = humpToUnderline(attributeName);
                    Object val = field.get(query);
                    if (ToolUtil.isNull(val) || "".equals(val)) {
                        continue;
                    }
                    //模糊多字段
                    if (ToolUtil.isNotBlank(blurry)) {
                        String[] blurrys = blurry.split(",");
                        queryWrapper.and(wrapper -> {
                            for (int i = 0; i < blurrys.length; i++) {
                                String column = humpToUnderline(blurrys[i]);
                                wrapper.or();
                                wrapper.like(column, val.toString());
                            }
                        });
                        continue;
                    }
                    String finalAttributeName = attributeName;
                    switch (queryAnno.type()) {
                        case EQUAL:
                            queryWrapper.eq(attributeName, val);
                            break;
                        case GREATER_THAN:
                            queryWrapper.ge(finalAttributeName, val);
                            break;
                        case LESS_THAN:
                            queryWrapper.le(finalAttributeName, val);
                            break;
                        case LESS_THAN_NQ:
                            queryWrapper.lt(finalAttributeName, val);
                            break;
                        case INNER_LIKE:
                            queryWrapper.like(finalAttributeName, val);
                            break;
                        case LEFT_LIKE:
                            queryWrapper.likeLeft(finalAttributeName, val);
                            break;
                        case RIGHT_LIKE:
                            queryWrapper.likeRight(finalAttributeName, val);
                            break;
                        case IN:
                            if (CollUtil.isNotEmpty((Collection<Long>) val)) {
                                queryWrapper.in(finalAttributeName, (Collection<Long>) val);
                            }
                            break;
                        case NOT_EQUAL:
                            queryWrapper.ne(finalAttributeName, val);
                            break;
                        case NOT_NULL:
                            queryWrapper.isNotNull(finalAttributeName);
                            break;
                        case BETWEEN:
                            String[] BETWEEN = ((String) val).split(",");
                            queryWrapper.between(finalAttributeName, BETWEEN[0], BETWEEN[1]);
                            break;
                        case UNIX_TIMESTAMP:
                            String[] UNIX_TIMESTAMP = ((String) val).split(",");
                            if (ToolUtil.isNotNull(UNIX_TIMESTAMP)) {
                                Date start = TimeUtil.parse(UNIX_TIMESTAMP[0], null);
                                Date end = TimeUtil.parse(UNIX_TIMESTAMP[1], null);
                                queryWrapper.between(finalAttributeName, start, end);
                            }
                            break;
                        default:
                            break;
                    }
                }
                field.setAccessible(accessible);
            }
        } catch (Exception e) {
            PrintUtil.error("[Mybatis Plus条件构建] 发生异常，异常信息为：{}", e.getMessage());
            throw new TransactionException(e.getMessage());
        }
        return queryWrapper;
    }

    /**
     * 获取字段条件
     * @param clazz
     * @param fields
     * @return List<Field>
     */
    private List<Field> getAllFields(Class clazz, List<Field> fields) {
        if (ToolUtil.isNotNull(clazz)) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            getAllFields(clazz.getSuperclass(), fields);
        }
        return fields;
    }

    /**
     * 驼峰命名转为下划线命名
     * @param field 驼峰命名字符串
     * @return String
     */
    private String humpToUnderline(String field) {
        StringBuilder sb = new StringBuilder(field);
        int temp = 0; //定位
        if (!field.contains("_")) {
            for (int i = 0; i < field.length(); i++) {
                if (Character.isUpperCase(field.charAt(i))) {
                    sb.insert(i + temp, "_");
                    temp += 1;
                }
            }
        }
        return sb.toString();
    }

}
