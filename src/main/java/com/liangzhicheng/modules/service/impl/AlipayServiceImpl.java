package com.liangzhicheng.modules.service.impl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.liangzhicheng.common.exception.TransactionException;
import com.liangzhicheng.common.pay.alipay.properties.AlipayProperties;
import com.liangzhicheng.common.utils.PrintUtil;
import com.liangzhicheng.modules.service.IAlipayService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.HashMap;

/**
 * 支付宝服务实现类
 */
@Service
public class AlipayServiceImpl implements IAlipayService {

    @Resource
    private AlipayClient alipayClient;
    @Resource
    private AlipayProperties properties;

    @Override
    public String prepayAlipayAPP(String orderId) {
        AlipayTradeAppPayModel model = null; //设置model参数
        AlipayTradeAppPayRequest request = null; //设置请求参数
        AlipayTradeAppPayResponse response = null; //返回响应结果
        String outTradeNo = ""; //商户订单号，商户APP订单系统中唯一订单号，必填
        String totalAmount = ""; //付款金额，必填
        String subject = ""; //订单名称，必填
        String body = ""; //商品描述，可空
        String timeoutExpress = "30m"; //超时时限
        try {
            model = new AlipayTradeAppPayModel();
            model.setOutTradeNo(outTradeNo);
            model.setTotalAmount(totalAmount);
            model.setSubject(subject);
            model.setBody(body);
            model.setTimeoutExpress(timeoutExpress);
            request = new AlipayTradeAppPayRequest();
            request.setBizModel(model);
            request.setNotifyUrl(properties.getNotifyUrlApp());
            response = alipayClient.sdkExecute(request);
            PrintUtil.info("[支付宝预支付（APP）] 响应结果：{}", response.getBody());
            return response.getBody();
        } catch (AlipayApiException e) {
            PrintUtil.error("[支付宝预支付（APP）] 失败：{}" , e);
            throw new TransactionException("支付失败！");
        }
    }

    @Override
    public void prepayAlipayWEB(String orderId, HttpServletResponse response) {
        AlipayTradePagePayRequest request = null; //设置请求参数
        AlipayTradePagePayResponse pagePayResponse = null; //返回响应结果
        String outTradeNo = ""; //商户订单号，商户网站订单系统中唯一订单号，必填
        String totalAmount = ""; //付款金额，必填
        String subject = ""; //订单名称，必填
        String body = ""; //商品描述，可空
        String result = "";
        PrintWriter out = null;
        try {
            request = new AlipayTradePagePayRequest();
//            alipayRequest.setReturnUrl(alipayProperties.getReturnUrl());
            request.setNotifyUrl(properties.getNotifyUrlWeb());
            outTradeNo = "1";
            totalAmount = new BigDecimal(0.01).toString();
            subject = "名称";
            request.setBizContent("{\"out_trade_no\":\""+ outTradeNo +"\","
                    + "\"total_amount\":\""+ totalAmount +"\","
                    + "\"subject\":\""+ subject +"\","
                    + "\"body\":\""+ body +"\","
                    + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
            //若想给BizContent增加其他可选请求参数，以增加自定义超时时间参数timeout_express来举例说明
            //alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
            //		+ "\"total_amount\":\""+ total_amount +"\","
            //		+ "\"subject\":\""+ subject +"\","
            //		+ "\"body\":\""+ body +"\","
            //		+ "\"timeout_express\":\"10m\","
            //		+ "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
            pagePayResponse = alipayClient.pageExecute(request);
            PrintUtil.info("[支付宝预支付（网页）] 响应结果：{}", pagePayResponse.getBody());
            result = pagePayResponse.getBody();
            //输出
            response.setContentType("text/html;charset=utf-8");
            out = response.getWriter();
            out.println(result);
        } catch (AlipayApiException e) {
            PrintUtil.error("[支付宝预支付（网页）] 失败：{}" , e);
            throw new TransactionException("支付失败！");
        } catch (IOException e) {
            PrintUtil.error("[支付宝预支付（网页）] 输出异常：{}" , e);
            throw new TransactionException("支付失败！");
        }
    }

    @Override
    public String refundAlipay(String orderId) {
        JSONObject jsonObject = JSONUtil.createObj();
        jsonObject.putOpt("out_trade_no", orderId);
        jsonObject.putOpt("refund_amount", "0.01");
        try {
            AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
            request.setBizContent(jsonObject.toString());
            AlipayTradeRefundResponse response = alipayClient.execute(request);
            if(response.isSuccess()){
                //处理订单信息以及其他相关业务逻辑
                // TODO ...
                return "success";
            }else{
                PrintUtil.error("[支付宝退款] 失败，订单号为{}，body为{}",
                        response.getOutTradeNo(), response.getBody());
                throw new TransactionException("退款失败！");
            }
        } catch (AlipayApiException e) {
            PrintUtil.error("[支付宝退款] 失败：{}", e.getMessage());
            throw new TransactionException("退款失败！");
        }
    }

    @Override
    public String notifyUrlAPP(HashMap<String, String> params) {
        return this.notifyHandle(params);
    }

    @Override
    public String notifyUrlWEB(HashMap<String, String> params) {
        return this.notifyHandle(params);
    }

    /**
     * 异步通知回调统一处理
     * @param params
     * @return String
     */
    private String notifyHandle(HashMap<String, String> params){
        boolean signValidate = false; //验证签名
        String outTradeNo = ""; //商户订单号
        String tradeNo = ""; //支付宝交易号
        String tradeStatus = ""; //交易状态
        try {
            signValidate = AlipaySignature.rsaCheckV1(
                    params,
                    properties.getAlipayPublicKey(),
                    properties.getCharset(),
                    properties.getSignType());
            /**
             * 实际验证过程建议商户务必添加以下校验：
             * 1.需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号
             * 2.判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额）
             * 3.校验通知中的seller_id（或者seller_email) 是否为out_trade_no这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email）
             * 4.验证app_id是否为该商户本身
             */
            if (signValidate) {
                outTradeNo = params.get("out_trade_no");
                tradeNo = params.get("trade_no");
                tradeStatus = params.get("trade_status");
                if ("TRADE_FINISHED".equals(tradeStatus)) {
                    //判断该笔订单是否在商户网站中已经做过处理
                    //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                    //如果有做过处理，不执行商户的业务程序
                    //注意：
                    //退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
                } else if ("TRADE_SUCCESS".equals(tradeStatus)) {
                    //判断该笔订单是否在商户网站中已经做过处理
                    //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                    //如果有做过处理，不执行商户的业务程序
                    //处理订单信息以及其他相关业务逻辑
                    // TODO ...
                    //注意：
                    //付款完成后，支付宝系统发送该交易状态通知
                }
                return "success";
            } else { //验证失败
                //调试用，写文本函数记录程序运行情况是否正常
                //String sWord = AlipaySignature.getSignCheckContentV1(params);
                //AlipayConfig.logResult(sWord);
            }
        } catch (AlipayApiException e) {
            PrintUtil.error("[支付宝预支付] 异步通知回调失败：{}" , e);
            throw new TransactionException("支付失败！");
        }
        return "fail";
    }

}
