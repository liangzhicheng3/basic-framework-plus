package com.liangzhicheng.common.utils;

import cn.hutool.json.JSONException;
import cn.hutool.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信工具类
 * @author liangzhicheng
 */
public class WeChatUtil {

    private static final String APP_ID_RESOURCE;
    private static final String SECRET_RESOURCE;
    private static final String GRANT_TYPE_RESOURCE;
    private static final String AUTH_URL_RESOURCE;
    private static final String USER_INFO_URL_RESOURCE;

    static {
        APP_ID_RESOURCE = ResourceUtil.getValue("wechat.user.app_id");
        SECRET_RESOURCE = ResourceUtil.getValue("wechat.user.secret");
        GRANT_TYPE_RESOURCE = ResourceUtil.getValue("wechat.user.grant_type");
        AUTH_URL_RESOURCE = ResourceUtil.getValue("wechat.user.auth_url");
        USER_INFO_URL_RESOURCE = ResourceUtil.getValue("wechat.user.user_info_url");
    }

    /**
     * 根据网页授权code获取用户资料
     * @param code
     * @return String
     */
    public static String getUserInfoByCode(String code){
        String url = String.format("%sappid=%s&secret=%s&code=%s&grant_type=%s",
                AUTH_URL_RESOURCE, APP_ID_RESOURCE, SECRET_RESOURCE, code, GRANT_TYPE_RESOURCE);
        String result = HttpUtil.sendGet(url);
        try{
            JSONObject jsonObject = JSONUtil.parseObject(result, JSONObject.class);
            String openId = jsonObject.getStr("openid");
            String accessToken = jsonObject.getStr("access_token");
            return getUserInfoByOpenId(openId, accessToken);
        }catch(JSONException e){
            PrintUtil.info("[微信调用] 发生异常，异常信息为：{}", e.getMessage());
            return result;
        }
    }

    /**
     * 根据openId，accessToken获取用户信息
     * @param openId
     * @return String
     */
    public static String getUserInfoByOpenId(String openId, String accessToken){
        String url = String.format("%saccess_token=%s&openid=%s&lang=zh_CN",
                USER_INFO_URL_RESOURCE, accessToken, openId);
        return HttpUtil.sendGet(url);
    }

    /**
     * 根据code获取用户openId，accessToken
     * @param code
     * @return String
     */
    public static Map<String, Object> getOpenIdByCode(String code){
        String url = String.format("%sappid=%s&secret=%s&code=%s&grant_type=%s",
                AUTH_URL_RESOURCE, APP_ID_RESOURCE, SECRET_RESOURCE, code, GRANT_TYPE_RESOURCE);
        String result = HttpUtil.sendGet(url);
        Map<String, Object> map = new HashMap<>();
        try{
            JSONObject jsonObject = JSONUtil.parseObject(result, JSONObject.class);
            String openId = jsonObject.getStr("openid");
            String accessToken = jsonObject.getStr("access_token");
            map.put("openId", openId);
            map.put("accessToken", accessToken);
            return map;
        }catch(JSONException e){
            PrintUtil.info("[微信调用] 发生异常，异常信息为：{}", e.getMessage());
            map.put("result", result);
            return map;
        }
    }

}
