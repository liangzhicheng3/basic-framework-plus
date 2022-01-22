package com.liangzhicheng.common.utils;

import cn.hutool.json.JSONException;
import cn.hutool.json.JSONObject;
import com.liangzhicheng.common.bean.RedisBean;
import com.liangzhicheng.common.constant.Constants;
import com.liangzhicheng.config.context.SpringContextHolder;
import org.apache.shiro.crypto.hash.Sha1Hash;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;

/**
 * 微信工具类
 * @author liangzhicheng
 */
public class WeChatUtil {

    private static RedisBean redisBean = SpringContextHolder.getBean(RedisBean.class);

    private static final String APP_ID_RESOURCE;
    private static final String SECRET_RESOURCE;
    private static final String GRANT_TYPE_RESOURCE;
    private static final String AUTH_URL_RESOURCE;
    private static final String TOKEN_URL_RESOURCE;
    private static final String USER_INFO_URL_RESOURCE;
    private static final String TICKET_URL_RESOURCE;

    static {
        APP_ID_RESOURCE = ResourceUtil.getValue("wechat.user.app_id");
        SECRET_RESOURCE = ResourceUtil.getValue("wechat.user.secret");
        GRANT_TYPE_RESOURCE = ResourceUtil.getValue("wechat.user.grant_type");
        AUTH_URL_RESOURCE = ResourceUtil.getValue("wechat.user.auth_url");
        TOKEN_URL_RESOURCE = ResourceUtil.getValue("wechat.user.token_url");
        USER_INFO_URL_RESOURCE = ResourceUtil.getValue("wechat.user.user_info_url");
        TICKET_URL_RESOURCE = ResourceUtil.getValue("wechat.user.ticket_url");
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
            return getUserInfoByOpenId(openId);
        }catch(JSONException e){
            PrintUtil.info("[微信调用] 发生异常，异常信息为：{}", e.getMessage());
            return result;
        }
    }

    /**
     * 根据openId获取用户信息
     * @param openId
     * @return String
     */
    public static String getUserInfoByOpenId(String openId){
        String url = String.format("%saccess_token=%s&openid=%s&lang=zh_CN",
                USER_INFO_URL_RESOURCE, getAccessToken(), openId);
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

    /**
     * 刷新微信相关参数
     */
    public static void refresh(){
        refreshAccessToken();
        refreshJsapiTicket();
    }

    /**
     * 获取微信accessToken
     * @return String
     */
    public static String getAccessToken(){
        String accessToken = redisBean.get(Constants.WECHAT_ACCESS_TOKEN_KEY);
        if(ToolUtil.isBlank(accessToken)){
            accessToken = refreshAccessToken();
        }
        return accessToken;
    }

    /**
     * 获取微信的jsapi_ticket
     * @return String
     */
    public static String getJsapiTicket(){
        String ticket = redisBean.get(Constants.WECHAT_TICKET_KEY);
        if(ToolUtil.isBlank(ticket)){
            ticket = refreshJsapiTicket();
        }
        return ticket;
    }

    /**
     * 刷新微信的accessToken，正常情况下，微信会返回下述JSON数据包给公众号：{"access_token":"ACCESS_TOKEN","expires_in":7200}
     * 错误时微信会返回错误码等信息，JSON数据包示例如下（该示例为AppID无效错误）:{"errcode":40013,"errmsg":"invalid appid"}
     * @return String
     */
    public static String refreshAccessToken(){
        String url = String.format("%sgrant_type=client_credential&appid=%s&secret=%s",
                TOKEN_URL_RESOURCE, APP_ID_RESOURCE, SECRET_RESOURCE);
        String result = HttpUtil.sendGet(url);
        String accessToken = "";
        try{
            JSONObject jsonObject = JSONUtil.parseObject(result, JSONObject.class);
            accessToken = jsonObject.getStr("access_token");
            redisBean.set(Constants.WECHAT_ACCESS_TOKEN_KEY, accessToken);
        }catch(JSONException e){
            PrintUtil.info("[微信调用] 发生异常，异常信息为：{}", e.getMessage());
            return result;
        }
        return accessToken;
    }

    /**
     * 刷新jsapiTicket
     * @return String
     */
    public static String refreshJsapiTicket(){
        String url = String.format("%saccess_token=%s&type=jsapi",
                TICKET_URL_RESOURCE, getAccessToken());
        String result = HttpUtil.sendGet(url);
        String ticket = "";
        try{
            JSONObject jsonObject = JSONUtil.parseObject(result, JSONObject.class);
            ticket = jsonObject.getStr("ticket");
            redisBean.set(Constants.WECHAT_TICKET_KEY, ticket);
        }catch(JSONException e){
            PrintUtil.info("[微信调用] 发生异常，异常信息为：{}", e.getMessage());
        }
        return ticket;
    }

    /**
     * 签名（sha1算法）
     * @param map
     * @return String
     */
    public static String signSHA1(SortedMap<String, Object> map){
        StringBuffer splice = new StringBuffer();
        for(Map.Entry<String,Object> entry : map.entrySet()){
            String key = entry.getKey();
            String value = String.valueOf(entry.getValue());
            if(value != null && !value.equals("") && !key.equals("sign") && !key.equals("key")){
                splice.append(key + "=" + value + "&");
            }
        }
        String result = splice.toString();
        result = result.substring(0, result.length() - 1);
        return new Sha1Hash(result).toString();
    }

}
