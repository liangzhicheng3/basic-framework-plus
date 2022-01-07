package com.liangzhicheng.common.pay.alipay.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 支付宝参数实体类
 * @author liangzhicheng
 */
@Data
@ConfigurationProperties(prefix = "alipay")
@Component
public class AlipayProperties {

    /**
     * appid，应用id
     */
    private String appId;

    /**
     * 商户密钥
     */
    private String merchantPrivateKey;

    /**
     * 支付宝公钥，查看地址：https://openhome.alipay.com/platform/keyManage.htm，对应appid下的支付宝公钥。
     */
    private String alipayPublicKey;

    /**
     * 支付宝异步通知支付结果url（APP）
     */
    private String notifyUrlApp;

    /**
     * 支付宝异步通知支付结果url（网页）
     */
    private String notifyUrlWeb;

    /**
     * 支付宝同步通知url
     */
    private String returnUrl;

    /**
     * 签名方式
     */
    private String signType;

    /**
     * 字符编码格式
     */
    private String charset;

    /**
     * 支付宝网关
     */
    private String gatewayUrl;

}
