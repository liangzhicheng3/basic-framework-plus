package com.liangzhicheng.common.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import sun.misc.BASE64Decoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * AES加密、解密算法工具类
 * @author liangzhicheng
 */
public class AESUtil {

    private static final String AES_ALGORITHM;
    private static final String AES_SECRET;

    static {
        AES_ALGORITHM = ResourceUtil.getValue("aes.algorithm");
        AES_SECRET = ResourceUtil.getValue("aes.secret");
    }

    /**
     * AES加密
     * @param content
     * @return String
     */
    public static String aesEncrypt(String content) {
        try {
            return aesEncrypt(content, AES_SECRET);
        } catch (Exception e) {
            PrintUtil.error("AES加密有误：{}", e.getMessage());
            return "";
        }
    }

    /**
     * AES解密
     * @param encrypt
     * @return String
     */
    public static String aesDecrypt(String encrypt) {
        try {
            return aesDecrypt(encrypt, AES_SECRET);
        } catch (Exception e) {
            PrintUtil.error("AES解密有误：{}", e.getMessage());
            return "";
        }
    }

    /**
     * 从AES签名中获取参数
     * @param key
     * @param sign
     * @return String
     */
    public static String getParam(String key, String sign){
        if(ToolUtil.isNotBlank(key, sign)) {
            try {
                Map<String, String> map = signToMap(sign);
                return map.get(key);
            } catch (Exception e) {
                PrintUtil.error("AES签名中获取参数有误：{}", e.getMessage());
                return "";
            }
        }
        return null;
    }

    /**
     * 将AES签名后的字符串转成map
     * @param sign
     * @return Map
     */
    public static Map<String, String> signToMap(String sign) {
        try {
            Map<String, String> map = new HashMap<>();
            String string = aesDecrypt(sign, AES_SECRET);
            String[] str = string.split("&");
            for (int i = 0; i < str.length; i++) {
                String key = str[i].substring(0, str[i].indexOf("="));
                String value = str[i].substring(str[i].indexOf("=") + 1);
                map.put(key, value);
            }
            return map;
        }catch(Exception e) {
            PrintUtil.error("AES签名后的字符串转成Map有误：{}", e.getMessage());
            return null;
        }
    }

    /**
     * AES加密为base64Code
     * @param content 待加密的内容
     * @param encryptKey 加密密钥
     * @return 加密后的base64Code
     * @throws Exception
     */
    public static String aesEncrypt(String content, String encryptKey) throws Exception {
        return base64Encode(aesEncryptToBytes(content, encryptKey));
    }

    /**
     * 将base64Code AES解密
     * @param encryptStr 待解密base64Code
     * @param decryptKey 解密密钥
     * @return 解密后的String
     * @throws Exception
     */
    public static String aesDecrypt(String encryptStr, String decryptKey) throws Exception {
        return StringUtils.isEmpty(encryptStr) ? null : aesDecryptByBytes(base64Decode(encryptStr), decryptKey);
    }

    /**
     * base64Encode加密
     * @param bytes 待编码的byte[]
     * @return 编码后的base 64 code
     */
    public static String base64Encode(byte[] bytes){
        return Base64.encodeBase64String(bytes);
    }

    /**
     * base64Decode解密
     * @param base64Code 待解码的base 64 code
     * @return 解码后的byte[]
     * @throws Exception
     */
    public static byte[] base64Decode(String base64Code) throws Exception{
        return StringUtils.isEmpty(base64Code) ? null : new BASE64Decoder().decodeBuffer(base64Code);
    }

    /**
     * AES加密
     * @param content 待加密的内容
     * @param encryptKey 加密密钥
     * @return 加密后的byte[]
     * @throws Exception
     */
    public static byte[] aesEncryptToBytes(String content, String encryptKey) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128);
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(encryptKey.getBytes(), "AES"));

        return cipher.doFinal(content.getBytes("utf-8"));
    }

    /**
     * AES解密
     * @param encryptBytes 待解密的byte[]
     * @param decryptKey 解密密钥
     * @return 解密后的String
     * @throws Exception
     */
    public static String aesDecryptByBytes(byte[] encryptBytes, String decryptKey) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128);
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(decryptKey.getBytes(), "AES"));
        byte[] decryptBytes = cipher.doFinal(encryptBytes);
        return new String(decryptBytes);
    }

    /*public static void main(String[] args) {
        String sign = aesEncrypt("orderId=1122&userId=123");
        System.out.println(sign);
    }*/

}
