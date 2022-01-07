package com.liangzhicheng.modules.controller;

import cn.hutool.core.codec.Base64;
import cn.hutool.json.JSONObject;
import com.liangzhicheng.common.basic.BaseController;
import com.liangzhicheng.common.bean.RedisBean;
import com.liangzhicheng.common.constant.ApiConstant;
import com.liangzhicheng.common.push.PushFactory;
import com.liangzhicheng.common.response.ResponseResult;
import com.liangzhicheng.common.utils.*;
import com.liangzhicheng.config.mvc.interceptor.annotation.LoginValidate;
import com.liangzhicheng.modules.entity.AreaNameEntity;
import com.liangzhicheng.modules.entity.dto.LoginClientDTO;
import com.liangzhicheng.modules.entity.dto.LoginWeChatDTO;
import com.liangzhicheng.modules.service.IAreaNameService;
import io.swagger.annotations.*;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.InvalidParameterSpecException;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Api(value="ClientLoginController", tags={"登录相关控制器"})
@RestController
@RequestMapping("/client")
public class ClientLoginController extends BaseController {

    @Resource
    private RedisBean redisBean;
    @Resource
    private IAreaNameService areaNameService;

    @ApiOperation(value = "获取短信验证码，不需要token")
    @PostMapping(value = "/sendSMS")
    @ApiOperationSupport(ignoreParameters = {"loginClientDTO.userId",
            "loginClientDTO.vcode"})
    @ApiResponses({@ApiResponse(code = ApiConstant.BASE_SUCCESS_CODE,
            message = "成功", response = String.class)})
    public ResponseResult sendSMS(@RequestBody LoginClientDTO loginClientDTO){
        String phone = loginClientDTO.getPhone();
        AssertUtil.isFalse(ToolUtil.isBlank(phone)
                || !ToolUtil.isPhone(phone), ApiConstant.PARAM_PHONE_ERROR);
        String vcode = ToolUtil.generateRandom();
        SmsUtil.sendSMS(phone, vcode);
        redisBean.set(phone, vcode, 75 << 2, TimeUnit.SECONDS);
        return buildSuccessInfo("发送成功！");
    }

    @ApiOperation(value = "APP手机号码登录，不需要token")
    @PostMapping(value = "/loginPhone")
    @ApiOperationSupport(ignoreParameters = {"loginClientDTO.userId", "loginClientDTO.email"})
    @ApiResponses({@ApiResponse(code = ApiConstant.BASE_SUCCESS_CODE, message = "成功", response = String.class)})
    public ResponseResult loginPhone(@RequestBody LoginClientDTO loginClientDTO){
        String phone = loginClientDTO.getPhone();
        String vcode = loginClientDTO.getVcode();
        AssertUtil.isFalse(ToolUtil.isBlank(phone, vcode), ApiConstant.PARAM_IS_NULL);
        AssertUtil.isFalse(!ToolUtil.isPhone(phone), ApiConstant.PARAM_PHONE_ERROR);
        String existVcode = redisBean.get(phone);
        AssertUtil.isFalse(ToolUtil.isBlank(existVcode)
                || !existVcode.equals(vcode), ApiConstant.PARAM_VCODE_ERROR);
        //用户信息逻辑
        //同一台设备登录推送下线通知 (deviceToken:'设备号 必传',appType:'IOS或ANDROID 必传')
//        Map<String, Object> pushMap = new HashMap<>(3);
//        pushMap.put("userId", user.getUserId());
//        pushMap.put("token", deviceToken);
//        pushMap.put("appType", appType);
//        PushFactory.getInstance().build().updateToken(pushMap);
        //生成JSON Web Token
        Date expireTime = TimeUtil.dateAdd(new Date(), 1);
        String token = JWTUtil.createTokenMINI("6688", expireTime);
        JWTUtil.updateTokenMINI("6688", token);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("user", "user");
        resultMap.put("token", token);
        return buildSuccessInfo(resultMap);
    }

    @ApiOperation(value = "小程序授权登录，不需要token")
    @PostMapping(value = "/loginMINI")
    @ApiResponses({@ApiResponse(code = ApiConstant.BASE_SUCCESS_CODE, message = "成功", response = String.class)})
    public ResponseResult loginMINI(@Validated @RequestBody LoginWeChatDTO loginWeChatDTO,
                                    BindingResult bindingResult){
        String code = loginWeChatDTO.getCode();
        String encryptedData = loginWeChatDTO.getEncryptedData();
        String iv = loginWeChatDTO.getIv();
        AssertUtil.isFalse(ToolUtil.isBlank(code, encryptedData, iv), ApiConstant.PARAM_IS_NULL);
        Map<String, Object> miniMap = new HashMap<>();
        miniMap.put("appid", ResourceUtil.getValue("wechat.user.app_id"));
        miniMap.put("secret", ResourceUtil.getValue("wechat.user.secret"));
        miniMap.put("js_code", code);
        miniMap.put("grant_type", "authorization_code");
        JSONObject jsonObject = JSONUtil.parseObject(
                HttpUtil.sendPost(ResourceUtil.getValue("wechat.user.login_url"), miniMap), JSONObject.class);
        String sessionKey = jsonObject.getStr("session_key");
        JSONObject userInfo = this.getUserInfo(sessionKey, encryptedData, iv);
        String avatar = userInfo.getStr("avatarUrl");
        String nickName = userInfo.getStr("nickName");
        String gender = userInfo.getStr("gender");
        String country = userInfo.getStr("country");
        String province = userInfo.getStr("province");
        String city = userInfo.getStr("city");
        /**
         * 保存用户信息 save(user);
         * 1.根据微信授权后返回用户信息获取openId
         * 2.根据openId查询用户信息记录是否存在，不存在则新增
         */
        String openId = jsonObject.getStr("openid");

        //地区处理
        AreaNameEntity entity = new AreaNameEntity();
        entity.setCountry(country);
        entity.setProvince(province);
        entity.setCity(city);
        List<Map<String, Object>> resultList = areaNameService.getArea(entity);
        String areaName = "";
        String areaCode = "";
        if (resultList != null && resultList.size() > 0) {
            areaName = (String) resultList.get(0).get("areaName");
            areaCode = ((String) resultList.get(0).get("areaCode")).substring(5);
//            user.setCountryName(areaName);
//            user.setCountryId(areaCode);
            if (resultList.size() > 1) {
                areaName = (String) resultList.get(1).get("areaName");
                areaCode = ((String) resultList.get(1).get("areaCode")).substring(5);
//                user.setProvinceName(areaName);
//                user.setProvinceId(areaCode);
            }
            if (resultList.size() > 2) {
                areaName = (String) resultList.get(2).get("areaName");
                areaCode = ((String) resultList.get(2).get("areaCode")).substring(5);
//                user.setCityName(areaName);
//                user.setCityId(areaCode);
            }
        }

        //生成JSON Web Token
        Date expireTime = TimeUtil.dateAdd(new Date(), 1);
        String token = JWTUtil.createTokenMINI("6688", expireTime);
        JWTUtil.updateTokenMINI("6688", token);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("user", userInfo);
        resultMap.put("token", token);
        return buildSuccessInfo(resultMap);
    }

    @ApiOperation(value = "APP微信登录")
    @PostMapping(value = "/loginAPP")
    @ApiOperationSupport(ignoreParameters = {"loginWeChatDTO.encryptedData", "loginWeChatDTO.iv"})
    @ApiResponses({@ApiResponse(code = ApiConstant.BASE_SUCCESS_CODE, message = "成功", response = String.class)})
    public ResponseResult loginAPP(@Validated @RequestBody LoginWeChatDTO loginWeChatDTO,
                                   BindingResult bindingResult){
        String code = loginWeChatDTO.getCode();
        /**
         * 1.根据code获取用户信息
         * 2.根据openId判断用户是否存在，存在直接返回用户信息，不存在新增用户信息
         */
        String result = WeChatUtil.getUserInfoByCode(code);
        JSONObject userInfo = JSONUtil.parseObject(result, JSONObject.class);

        Date expireTime = TimeUtil.dateAdd(new Date(), 1);
        String token = JWTUtil.createTokenMINI("6688", expireTime);
        JWTUtil.updateTokenMINI("6688", token);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("user", "user");
        resultMap.put("token", token);
        return buildSuccessInfo(resultMap);
    }

    @LoginValidate
    @ApiOperation(value = "APP退出登录")
    @PostMapping(value = "/logOutAPP")
    @ApiOperationSupport(ignoreParameters = {"loginClientDTO.phone", "loginClientDTO.vcode"})
    @ApiResponses({@ApiResponse(code = ApiConstant.BASE_SUCCESS_CODE, message = "成功", response = String.class)})
    public ResponseResult logOutAPP(@RequestBody LoginClientDTO loginClientDTO){
        String userId = loginClientDTO.getUserId();
        //判断用户是否存在
        // TODO user
        //清除缓存中账号及设备登录类型
        //XingePush.clearTokenByLogout(userId);
        //XingePush.clearTypeByLogout(userId);
        JWTUtil.clearTokenMINI(userId);
        return buildSuccessInfo("退出成功！");
    }

    /**
     * 根据会话密钥、加密数据获取用户信息
     * @param sessionKey
     * @param encryptedData
     * @param iv
     * @return JSONObject
     */
    private JSONObject getUserInfo(String sessionKey, String encryptedData, String iv) {
        //加密秘钥
        byte[] keyB = Base64.decode(sessionKey);
        //被加密的数据
        byte[] dataB = Base64.decode(encryptedData);
        //加密算法初始向量
        byte[] ivB = Base64.decode(iv);
        try {
            int base = 16;//密钥不足16位，补足
            if(keyB.length % base != 0){
                int groups = keyB.length / base + (keyB.length % base != 0 ? 1 : 0);
                byte[] temp = new byte[groups * base];
                Arrays.fill(temp, (byte) 0);
                System.arraycopy(keyB, 0, temp, 0, keyB.length);
                keyB = temp;
            }
            //初始化
            Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding","BC");
            SecretKeySpec spec = new SecretKeySpec(keyB, "AES");
            AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
            parameters.init(new IvParameterSpec(ivB));
            cipher.init(Cipher.DECRYPT_MODE, spec, parameters);
            byte[] resultByte = cipher.doFinal(dataB);
            if(resultByte != null && resultByte.length > 0){
                String result = new String(resultByte, "UTF-8");
                return JSONUtil.parseObject(result, JSONObject.class);
            }
        } catch (NoSuchAlgorithmException e) {
            PrintUtil.info("[微信授权登录] NoSuchAlgorithmException：{}", e.getMessage());
        } catch (NoSuchPaddingException e) {
            PrintUtil.info("[微信授权登录] NoSuchPaddingException：{}", e.getMessage());
        } catch (InvalidParameterSpecException e) {
            PrintUtil.info("[微信授权登录] InvalidParameterSpecException：{}", e.getMessage());
        } catch (IllegalBlockSizeException e) {
            PrintUtil.info("[微信授权登录] IllegalBlockSizeException：{}", e.getMessage());
        } catch (BadPaddingException e) {
            PrintUtil.info("[微信授权登录] BadPaddingException：{}", e.getMessage());
        } catch (UnsupportedEncodingException e) {
            PrintUtil.info("[微信授权登录] UnsupportedEncodingException：{}", e.getMessage());
        } catch (InvalidKeyException e) {
            PrintUtil.info("[微信授权登录] InvalidKeyException：{}", e.getMessage());
        } catch (InvalidAlgorithmParameterException e) {
            PrintUtil.info("[微信授权登录] InvalidAlgorithmParameterException：{}", e.getMessage());
        } catch (NoSuchProviderException e) {
            PrintUtil.info("[微信授权登录] NoSuchProviderException：{}", e.getMessage());
        }
        return null;
    }

}

