package com.liangzhicheng.common.pay.wechat.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 微信支付参数实体类
 * @author liangzhicheng
 */
@Data
@ConfigurationProperties(prefix = "wechat.pay")
@Component
public class WeChatPayProperties {

    /**
     * appid，应用id
     */
    private String appId;

    /**
     * 商户密钥
     */
    private String secret;

    /**
     * 微信支付商户号
     */
    private String mchId;

    /**
     * 支付签名
     */
    private String paySign;

    /**
     * 交易类型（APP）
     */
    private String tradeTypeApp;

    /**
     * 交易类型（小程序）
     */
    private String tradeTypeMini;

    /**
     * 统一下单url
     */
    private String requestUrl;

    /**
     * 微信支付异步通知支付结果url（APP）
     */
    private String notifyUrlApp;

    /**
     * 微信支付异步通知支付结果url（小程序）
     */
    private String notifyUrlMini;

    /**
     * 订单查询url
     */
    private String orderQueryUrl;

    /**
     * 退款证书
     */
    private String certName;

    /**
     * 退款url
     */
    private String refundUrl;

    /**
     * 退款查询url
     */
    private String refundQueryUrl;

}
