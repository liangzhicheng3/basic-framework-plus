package com.liangzhicheng.common.push.service;

import cn.hutool.core.map.MapUtil;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.push.model.v20160801.PushRequest;
import com.aliyuncs.push.model.v20160801.PushResponse;
import com.liangzhicheng.common.push.properties.Push;
import com.liangzhicheng.common.utils.PrintUtil;
import com.liangzhicheng.common.utils.ToolUtil;

import java.util.Map;

/**
 * 阿里推送服务类
 * @author liangzhicheng
 */
public class AliPushService extends BasePushService {

    private IClientProfile profile;
    private IAcsClient client;
    private PushRequest pushRequest;

    public AliPushService(Push push) {
        this.push = push;
        init();
    }

    private void init() {
        profile = DefaultProfile.getProfile("cn-hangzhou", push.getAliAccessKey(), push.getAliSecretKey());
        client = new DefaultAcsClient(profile);
        pushRequest = new PushRequest();
    }

    @Override
    public void push(Map<String, Object> paramsMap) {
        if("IOS".equals(MapUtil.getStr(paramsMap, "appType"))){
            pushRequest.setAppKey(push.getAliIosAppKey());
            /**
             * 推送配置：IOS
             * IOS的通知是通过APNs中心来发送的，需要填写对应的环境信息
             * "DEV"：表示开发环境，"PRODUCT"：表示生产环境
             */
            pushRequest.setIOSApnsEnv("DEV");
        }else{
            pushRequest.setAppKey(push.getAliAndroidAppKey());
        }
        pushRequest.setPushType("NOTICE"); //消息类型：MESSAGE NOTICE
        pushRequest.setDeviceType("ALL"); //设备类型：ANDROID IOS
        /**
         * 推送目标：
         *         DEVICE->根据设备推送
         *         ACCOUNT->根据账号推送
         *         ALIAS->根据别名推送
         *         TAG->根据标签推送
         *         ALL->推送给全部设备
         */
        pushRequest.setTarget("DEVICE");
        /**
         * 根据Target来设定，多个值使用逗号分隔，超过限制需要分多次推送
         * Target=DEVICE，值如deviceid111,deviceid1111（最多支持1000个）
         * Target=ACCOUNT，值如account111,account222（最多支持1000个）
         * Target=ALIAS，值如alias111,alias222（最多支持1000个）
         * Target=TAG，支持单Tag和多Tag，格式请参见标签格式https://help.aliyun.com/document_detail/48055.html?spm=a2c4g.11186623.0.0.54f128ebLzvWfW
         * Target=ALL，值为ALL
         */
        pushRequest.setTargetValue(MapUtil.getStr(paramsMap, "token"));
        pushRequest.setBody(MapUtil.getStr(paramsMap, "content")); //消息内容
        pushRequest.setTitle(MapUtil.getStr(paramsMap, "title")); //消息标题
        String params = MapUtil.getStr(paramsMap, "params");
        if(ToolUtil.isNotBlank(params)) {
            pushRequest.setIOSExtParameters(params);
            pushRequest.setAndroidExtParameters(params);
        }
        try {
            PushResponse pushResponse = client.getAcsResponse(pushRequest);
            PrintUtil.info("[阿里推送] 请求成功：RequestId->{},MessageId->{}", pushResponse.getRequestId(), pushResponse.getMessageId());
        } catch (ServerException e) {
            PrintUtil.error("[阿里推送] 发生异常，异常信息为：{}", e.getMessage());
        } catch (ClientException e) {
            PrintUtil.error("[阿里推送] 发生异常，异常信息为：{}", e.getMessage());
        }
    }

    @Override
    public void pushAll(Map<String, Object> paramsMap) {

    }

    @Override
    public void updateToken(Map<String, Object> paramsMap) {

    }

}
