package com.liangzhicheng.modules.service;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

/**
 * 支付宝服务类
 * @author liangzhicheng
 */
public interface IAlipayService {

    /**
     * 支付宝预支付（APP）
     * @param orderId
     * @return String
     */
    String prepayAlipayAPP(String orderId);

    /**
     * 支付宝预支付（网页）
     * @param orderId
     * @param response
     */
    void prepayAlipayWEB(String orderId, HttpServletResponse response);

    /**
     * 支付宝退款
     * @param orderId
     * @return String
     */
    String refundAlipay(String orderId);

    /**
     * 支付宝预支付异步通知回调（APP）
     * @param params
     * @return String
     */
    String notifyUrlAPP(HashMap<String, String> params);

    /**
     *
     * @param params
     * @return String
     */
    String notifyUrlWEB(HashMap<String, String> params);

}
