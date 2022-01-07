package com.liangzhicheng.modules.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 微信支付服务类
 * @author liangzhicheng
 */
public interface IWeChatPayService {

    /**
     * 微信预支付（APP）
     * @param orderId
     * @return Map<Object, Object>
     */
    Map<Object, Object> prepayWeChatAPP(String orderId);

    /**
     * 微信预支付（小程序）
     * @param orderId
     * @return Map<Object, Object>
     */
    Map<Object, Object> prepayWeChatMINI(String orderId);

    /**
     * 微信支付退款
     * @param orderId
     */
    String refundWeChat(String orderId);

    /**
     * 微信预支付异步通知回调（APP）
     * @param request
     * @param response
     * @return
     */
    String notifyUrlAPP(HttpServletRequest request, HttpServletResponse response);

    /**
     * 微信预支付异步通知回调（小程序）
     * @return String
     */
    String notifyUrlMINI(HttpServletRequest request, HttpServletResponse response);

}
