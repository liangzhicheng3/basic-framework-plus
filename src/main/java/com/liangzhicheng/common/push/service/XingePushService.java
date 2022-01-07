package com.liangzhicheng.common.push.service;

import cn.hutool.core.map.MapUtil;
import com.google.common.collect.Lists;
import com.liangzhicheng.common.bean.RedisBean;
import com.liangzhicheng.common.push.properties.Push;
import com.liangzhicheng.common.utils.JSONUtil;
import com.liangzhicheng.common.utils.PrintUtil;
import com.liangzhicheng.common.utils.ResourceUtil;
import com.liangzhicheng.common.utils.ToolUtil;
import com.tencent.xinge.XingeApp;
import com.tencent.xinge.bean.*;
import com.tencent.xinge.push.app.PushAppRequest;
import org.json.JSONObject;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 信鸽推送服务类
 * @author liangzhicheng
 */
public class XingePushService extends BasePushService {

    @Resource
    private RedisBean redisBean;

    public XingePushService(Push push) {
        this.push = push;
    }

    @Override
    public void push(Map<String, Object> paramsMap) {
        String appType = MapUtil.getStr(paramsMap, "appType");
        XingeApp xingeApp = config(appType);
        PushAppRequest pushAppRequest = new PushAppRequest();
        pushAppRequest.setAudience_type(AudienceType.account);
        pushAppRequest.setMessage_type(MessageType.notify);
        pushAppRequest.setAccount_push_type(1);
        Message message = new Message();
        //苹果、安卓参数处理
        handle(appType, pushAppRequest, message);
        message.setTitle(MapUtil.getStr(paramsMap, "title"));
        message.setContent(MapUtil.getStr(paramsMap, "content"));
        pushAppRequest.setMessage(message);
        ArrayList<String> accountList = Lists.newArrayList();
        accountList.add(MapUtil.getStr(paramsMap, "token"));
        pushAppRequest.setAccount_list(accountList);
        JSONObject result = xingeApp.pushApp(pushAppRequest);
        PrintUtil.info("[信鸽推送] 请求成功，响应参数：{}", JSONUtil.toJSONString(result));
    }

    @Override
    public void pushAll(Map<String, Object> paramsMap) {
        String appType = MapUtil.getStr(paramsMap, "appType");
        XingeApp xingeApp = config(appType);
        PushAppRequest pushAppRequest = new PushAppRequest();
        pushAppRequest.setAudience_type(AudienceType.all);
        pushAppRequest.setMessage_type(MessageType.notify);
        Message message = new Message();
        //苹果、安卓参数处理
        handle(appType, pushAppRequest, message);
        message.setTitle(MapUtil.getStr(paramsMap, "title"));
        message.setContent(MapUtil.getStr(paramsMap, "content"));
        pushAppRequest.setMessage(message);
        JSONObject result = xingeApp.pushApp(pushAppRequest);
        PrintUtil.info("[信鸽推送] 请求成功，响应参数：{}", result.toString());
    }

    @Override
    public void updateToken(Map<String, Object> paramsMap) {
        String userId = paramsMap.get("userId").toString();
        String token = paramsMap.get("token").toString();
        String appType = paramsMap.get("appType").toString();
        String oldToken = clearToken(userId);
        String oldType = clearType(userId);
        redisBean.hSet(push.getPushTokenKeyMap(), userId, token);
        redisBean.hSet(push.getPushTypeKeyMap(), userId, appType);
        //判断旧token不等于新token，推送下线消息
        if(ToolUtil.isNotBlank(oldToken) && !oldToken.equals(token)) {
            paramsMap = new HashMap<>(4);
            paramsMap.put("appType", oldType);
            paramsMap.put("token", oldToken);
            paramsMap.put("title", "下线通知");
            paramsMap.put("content", "您已在其他设备登录");
            push(paramsMap);
        }
    }

    /**
     * 与TPNS后台交互接口，由XingeApp.Builder进行构建
     * @param appType
     * @return XingeApp
     */
    private XingeApp config(String appType){
        String accessId = "";
        String secretKey = "";
        if("IOS".equals(appType)){
            accessId = push.getXingeIosAccessKey();
            secretKey = push.getXingeIosSecretKey();
        }else{
            accessId = push.getXingeAndroidAccessKey();
            secretKey = push.getXingeAndroidSecretKey();
        }
        XingeApp xingeApp = new XingeApp.Builder()
                .appId(accessId) //推送目标accessID（可在【产品管理】页面获取）
                .secretKey(secretKey)
                .build();
        return xingeApp;
    }

    private void handle(String appType, PushAppRequest pushAppRequest, Message message){
        MessageIOS messageIOS = null;
        MessageAndroid messageAndroid = null;
        if("IOS".equals(appType)){
            pushAppRequest.setPlatform(Platform.ios);
            if(ResourceUtil.getValue("spring.profiles.active").equals("master")){
                pushAppRequest.setEnvironment(Environment.product);
            }else{
                pushAppRequest.setEnvironment(Environment.dev);
            }
            messageIOS = new MessageIOS();
            message.setIos(messageIOS);
        }else{
            pushAppRequest.setPlatform(Platform.android);
            messageAndroid = new MessageAndroid();
            message.setAndroid(messageAndroid);
        }
    }

    /**
     * 清除用户token
     * @param userId
     * @return String
     */
    private String clearToken(String userId){
        String tokenKeyMap = push.getPushTokenKeyMap();
        String oldToken = redisBean.hGet(tokenKeyMap, userId);
        redisBean.hDelete(tokenKeyMap, userId);
        return oldToken;
    }

    /**
     * 清除用户设备
     * @param userId
     * @return String
     */
    private String clearType(String userId){
        String typeKeyMap = push.getPushTypeKeyMap();
        String oldType = redisBean.hGet(typeKeyMap, userId);
        PrintUtil.info("[客户端登录] 清除用户设备前应用类型为：{}", oldType);
        redisBean.hDelete(typeKeyMap, userId);
        return oldType;
    }

}
