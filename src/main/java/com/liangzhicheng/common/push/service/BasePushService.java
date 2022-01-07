package com.liangzhicheng.common.push.service;

import com.liangzhicheng.common.push.properties.Push;

import java.util.Map;

/**
 * 推送基础服务类（支持信鸽、阿里云）
 * @author liangzhicheng
 */
public abstract class BasePushService {

    Push push;

    /**
     * 推送
     * @param paramsMap token 设备、title 标题、content 内容、params 参数、appType 应用类型
     */
    public abstract void push(Map<String, Object> paramsMap);

    /**
     * 推送全部
     * @param paramsMap token 设备、title 标题、content 内容、params 参数、appType 应用类型
     */
    public abstract void pushAll(Map<String, Object> paramsMap);

    /**
     * 更新用户token
     * @param paramsMap 用户id、设备号、应用类型
     */
    public abstract void updateToken(Map<String, Object> paramsMap);

}
