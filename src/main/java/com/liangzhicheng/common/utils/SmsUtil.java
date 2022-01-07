package com.liangzhicheng.common.utils;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

/**
 * 云之讯短信获取工具类
 * @author liangzhicheng
 */
public class SmsUtil {

    private static final String SID;
    private static final String TOKEN;
    private static final String APP_ID;
    private static final String TEMPLATE_ID;
    private static final String URL;

    static {
        SID = ResourceUtil.getValue("sms.sid");
        TOKEN = ResourceUtil.getValue("sms.token");
        APP_ID = ResourceUtil.getValue("sms.app-id");
        TEMPLATE_ID = ResourceUtil.getValue("sms.template-id");
        URL = ResourceUtil.getValue("sms.url");
    }

    public static void sendSMS(String phone, String ... vcodes) {
        String content = "";
        if(ToolUtil.isNotBlank(vcodes)){
            for(String str : vcodes){
                content += (str + ",");
            }
            content = content.substring(0,content.length() - 1);
        }
        JSONObject jsonObject = JSONUtil.createObj();
        jsonObject.putOpt("sid", SID);
        jsonObject.putOpt("token", TOKEN);
        jsonObject.putOpt("appid", APP_ID);
        jsonObject.putOpt("templateid", TEMPLATE_ID);
        jsonObject.putOpt("mobile", phone);
        jsonObject.putOpt("param", content);
        jsonObject.putOpt("uid", null);
        try {
            String result = HttpUtil.sendPost(URL, jsonObject.toString());
            PrintUtil.info("[短信调用] 请求成功：phone->{},content->{},result->{}", phone, content, result);
        } catch (Exception e) {
            PrintUtil.error("[短信调用] 发生异常，异常信息为：{}", e.getMessage());
        }
    }

}
