package com.liangzhicheng.modules.service.impl;

import cn.hutool.core.map.MapUtil;
import com.liangzhicheng.common.constant.Constants;
import com.liangzhicheng.common.exception.TransactionException;
import com.liangzhicheng.common.pay.wechat.properties.RefundProperties;
import com.liangzhicheng.common.pay.wechat.properties.WeChatPayProperties;
import com.liangzhicheng.common.pay.wechat.singleton.WechatSingleton;
import com.liangzhicheng.common.pay.wechat.utils.MD5Util;
import com.liangzhicheng.common.pay.wechat.utils.XmlUtil;
import com.liangzhicheng.common.utils.HttpUtil;
import com.liangzhicheng.common.utils.PrintUtil;
import com.liangzhicheng.common.utils.TimeUtil;
import com.liangzhicheng.common.utils.ToolUtil;
import com.liangzhicheng.modules.service.IWeChatPayService;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 微信支付服务实现类
 * @author liangzhicheng
 */
@Service
public class WeChatPayServiceImpl implements IWeChatPayService {

    @Resource
    private HttpServletRequest request;
    @Resource
    private WeChatPayProperties properties;

    @Override
    public Map<Object, Object> prepayWeChatAPP(String orderId) {
        try {
            //构建请求参数
            Map<Object, Object> requestParams = this.buildRequestParams(orderId, properties.getTradeTypeApp());
            String xml = XmlUtil.mapToXmlStr(requestParams);
            PrintUtil.info("[微信预支付（APP）] 统一下单请求 xml：{}", xml);
            Map<String, Object> responseParams = XmlUtil.xmlStrToMap(this.sendOnce(properties.getRequestUrl(), xml));
            return this.responseHandle(responseParams, orderId);
        } catch (Exception e) {
            PrintUtil.error("[微信预支付（APP）] 失败：{}", e);
            throw new TransactionException("支付失败！");
        }
    }

    @Override
    public Map<Object, Object> prepayWeChatMINI(String orderId) {
        try {
            //构建请求参数
            Map<Object, Object> requestParams = this.buildRequestParams(orderId, properties.getTradeTypeMini());
            String xml = XmlUtil.mapToXmlStr(requestParams);
            PrintUtil.info("[微信预支付（小程序）] 统一下单请求 xml：{}", xml);
            Map<String, Object> responseParams = XmlUtil.xmlStrToMap(this.sendOnce(properties.getRequestUrl(), xml));
            return this.responseHandle(responseParams, orderId);
        } catch (Exception e) {
            PrintUtil.error("[微信预支付（小程序）] 失败：{}", e);
            throw new TransactionException("支付失败！");
        }
    }

    @Override
    public String refundWeChat(String orderId) {
        //订单号
        String outTradeNo = "";
        //订单金额
        Double orderMoney = 0.01;
        //退款金额
        Double refundMoney = 0.01;
        //转换金额格式
        BigDecimal bdOrderMoney = new BigDecimal(orderMoney, MathContext.DECIMAL32);
        BigDecimal bdRefundMoney = new BigDecimal(refundMoney, MathContext.DECIMAL32);
        try {
            //构建请求参数
            Map<Object, Object> requestParams = this.buildRequestParams(outTradeNo, bdOrderMoney, bdRefundMoney);
            String xml = XmlUtil.mapToXmlStr(requestParams);
            String responseParams = this.sendPost(xml, WechatSingleton.getSSLConnectionSocket());
            RefundProperties refundProperties = (RefundProperties) XmlUtil.xmlStrToBean(responseParams, RefundProperties.class);
            String returnCode = refundProperties.getReturn_code();
            String returnMsg = refundProperties.getReturn_msg();
            if(returnCode.equalsIgnoreCase("FAIL")){
                PrintUtil.error("[微信退款] 失败，returnMsg为：{}", returnMsg);
                throw new TransactionException("退款失败！");
            }else if(returnCode.equalsIgnoreCase("SUCCESS")){
                String resultCode = refundProperties.getResult_code();
                String errCodeDes = refundProperties.getErr_code_des();
                if(resultCode.equalsIgnoreCase("FAIL")){
                    PrintUtil.error("[微信退款] 失败，errCodeDes为：{}", errCodeDes);
                    throw new TransactionException("退款失败！");
                }else if(resultCode.equalsIgnoreCase("SUCCESS")){
                    //处理订单信息以及其他相关业务逻辑
                    // TODO ...
                    return "success";
                }
            }
        } catch (Exception e) {
            PrintUtil.error("[微信退款] 失败：{}", e.getMessage());
            throw new TransactionException("退款失败！");
        }
        return "fail";
    }

    @Override
    public String notifyUrlAPP(HttpServletRequest request, HttpServletResponse response) {
        return this.notifyHandle(request, response);
    }

    @Override
    public String notifyUrlMINI(HttpServletRequest request, HttpServletResponse response) {
        return this.notifyHandle(request, response);
    }

    /**
     * 得到请求微信支付请求参数
     * @param orderId 订单id
     * @return 请求参数map对象
     */
    private Map<Object, Object> buildRequestParams(String orderId, String tradeType){
        Map<Object, Object> params = new TreeMap<>();
        //微信分配的公众账号id（企业号corpid即为此appId或微信支付申请成功后在邮件中的appid）
        params.put("appid", properties.getAppId());
        //微信支付分配的商户号
        params.put("mch_id", properties.getMchId());
        //随机字符串，不长于32位，推荐随机数生成算法
        params.put("nonce_str", ToolUtil.generateRandomInteger(18).toUpperCase());
        //商户订单编号
        params.put("out_trade_no", orderId);
        //描述
        params.put("body", "xxx-支付");
        //支付金额
        params.put("total_fee", new BigDecimal(0.01).intValue());
        if("JSAPI".equals(tradeType)){
            //回调地址
            params.put("notify_url", properties.getNotifyUrlMini());
            //交易类型APP
            params.put("trade_type", properties.getTradeTypeMini());
            params.put("openid", "");
        }else if("APP".equals(tradeType)){
            //回调地址
            params.put("notify_url", properties.getNotifyUrlApp());
            //交易类型APP
            params.put("trade_type", properties.getTradeTypeApp());
        }
        params.put("spbill_create_ip", HttpUtil.getClientUrl(request));
        //签名前必须要参数全部写在前面
        //生成paySign，参考https://pay.weixin.qq.com/wiki/doc/api/wxa/wxa_api.php?chapter=7_7&index=5
        params.put("sign", this.encrypt(params, properties.getPaySign()));
        return params;
    }

    /**
     * 得到请求微信退款请求参数
     * @param outTradeNo 订单号
     * @param bdOrderMoney 格式化后订单金额
     * @param bdRefundMoney 格式化后退款金额
     * @return 请求参数map对象
     */
    private Map<Object, Object> buildRequestParams(String outTradeNo, BigDecimal bdOrderMoney, BigDecimal bdRefundMoney) {
        Map<Object, Object> params = new TreeMap<>();
        //微信分配的公众账号id（企业号corpid即为此appId或微信支付申请成功后在邮件中的appid）
        params.put("appid", properties.getAppId());
        //微信支付分配的商户号
        params.put("mch_id", properties.getMchId());
        //随机字符串，不长于32位，推荐随机数生成算法
        params.put("nonce_str", ToolUtil.generateRandomString(16));
        //下单生成的订单号
        params.put("out_trade_no", outTradeNo);
        //商户系统内部的退款单号，商户系统内部唯一，同一退款单号多次请求只退一笔
        params.put("out_refund_no", this.generateRefundNo());
        //订单总金额，单位为分
        params.put("total_fee", bdOrderMoney.multiply(new BigDecimal(100)).intValue());
        //退款总金额，订单总金额，单位为分
        params.put("refund_fee", bdRefundMoney.multiply(new BigDecimal(100)).intValue());
        //操作员帐号，默认为商户号
        params.put("op_user_id", properties.getMchId());
        //签名前必须要参数全部写在前面
        params.put("sign", this.encrypt(params, properties.getPaySign()));
        return params;
    }

    /**
     * 只请求一次，不做重试
     * @param url 地址
     * @param params 参数
     * @return 请求后结果
     * @throws Exception
     */
    private String sendOnce(final String url, String params) throws Exception {
        BasicHttpClientConnectionManager connManager;
        connManager = new BasicHttpClientConnectionManager(
                RegistryBuilder.<ConnectionSocketFactory>create()
                        .register("http", PlainConnectionSocketFactory.getSocketFactory())
                        .register("https", SSLConnectionSocketFactory.getSocketFactory())
                        .build(), null, null, null);
        HttpClient httpClient = HttpClientBuilder.create()
                .setConnectionManager(connManager)
                .build();
        HttpPost httpPost = new HttpPost(url);
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(5000)
                .setConnectTimeout(5000)
                .setConnectionRequestTimeout(10000).build();
        httpPost.setConfig(requestConfig);
        StringEntity postEntity = new StringEntity(params, "UTF-8");
        httpPost.addHeader("Content-Type", "text/xml");
        httpPost.addHeader("User-Agent", "wxpay sdk java v1.0 " + properties.getMchId());
        httpPost.setEntity(postEntity);
        HttpResponse httpResponse = httpClient.execute(httpPost);
        HttpEntity httpEntity = httpResponse.getEntity();
        String result = EntityUtils.toString(httpEntity, "UTF-8");
        PrintUtil.info("[微信预支付] 发送单次请求结果：{}", result);
        return result;
    }

    /**
     * 发送post请求到微信进行退款
     * @param xml
     * @param factory
     * @return 请求结果
     */
    private String sendPost(String xml, SSLConnectionSocketFactory factory) {
        PrintUtil.info("[微信支付] 退款请求 xml：{}", xml);
        HttpPost httPost = new HttpPost(properties.getRefundUrl());
        httPost.addHeader("Connection", "keep-alive");
        httPost.addHeader("Accept", "*/*");
        httPost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        httPost.addHeader("Host", "api.mch.weixin.qq.com");
        httPost.addHeader("X-Requested-With", "XMLHttpRequest");
        httPost.addHeader("Cache-Control", "max-age=0");
        httPost.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");
        httPost.setEntity(new StringEntity(xml, "UTF-8"));
        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(factory).build();
        CloseableHttpResponse response = null;
        String result = "";
        try {
            response = httpClient.execute(httPost);
            result = EntityUtils.toString(response.getEntity(), "UTF-8");
            PrintUtil.info("[微信支付] 退款响应：{}", result);
        } catch (Exception e) {
            PrintUtil.error("[微信支付] 退款发生异常，异常信息为：{}", e.getMessage());
            throw new TransactionException("退款失败！");
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                PrintUtil.error("[微信支付] 退款IO发生异常，异常信息为：{}", e.getMessage());
            }
        }
        return result;
    }

    /**
     * 支付响应结果处理
     * @param responseParams
     * @param orderId
     * @return Map<Object, Object>
     */
    private Map<Object, Object> responseHandle(Map<String, Object> responseParams, String orderId) {
        //响应报文
        String returnCode = MapUtil.getStr(responseParams, "return_code");
        String returnMsg = MapUtil.getStr(responseParams, "return_msg");
        if(returnCode.equalsIgnoreCase("FAIL")){
            PrintUtil.error("[微信预支付] 失败，returnMsg为：{}", returnMsg);
            throw new TransactionException("支付失败！");
        }else if(returnCode.equalsIgnoreCase("SUCCESS")){
            String resultCode = MapUtil.getStr(responseParams, "result_code");
            String errCodeDes = MapUtil.getStr(responseParams, "err_code_des");
            if(resultCode.equalsIgnoreCase("FAIL")){
                PrintUtil.error("[微信预支付] 失败，errCodeDes为：{}", errCodeDes);
                throw new TransactionException("支付失败！");
            }else if(resultCode.equalsIgnoreCase("SUCCESS")){
                String prepayId = MapUtil.getStr(responseParams, "prepay_id");
                Map<Object, Object> resultMap = new TreeMap<>();
                resultMap.put("appId", properties.getAppId());
                resultMap.put("timeStamp", TimeUtil.format(System.currentTimeMillis() / 1000, Constants.DATE_TIME_PATTERN));
                resultMap.put("nonceStr", ToolUtil.generateRandomString(32));
                resultMap.put("package", "prepay_id=" + prepayId);
                resultMap.put("signType", "MD5");
                resultMap.put("paySign", this.encrypt(resultMap, properties.getPaySign()));
                //处理订单信息以及其他相关业务逻辑
                // TODO ...
                return resultMap;
            }
        }
        return null;
    }

    /**
     * 根据签名加密请求参数
     * @param params 请求参数
     * @param paySign 加密值
     * @return 加密后
     */
    private String encrypt(Map<Object, Object> params, String paySign) {
        boolean encode = false;
        Set<Object> keysSet = params.keySet();
        Object[] keys = keysSet.toArray();
        Arrays.sort(keys);
        StringBuffer temp = new StringBuffer();
        boolean first = true;
        for (Object key : keys) {
            if (first) {
                first = false;
            } else {
                temp.append("&");
            }
            temp.append(key).append("=");
            Object value = params.get(key);
            String valueString = "";
            if (null != value) {
                valueString = value.toString();
            }
            if (encode) {
                try {
                    temp.append(URLEncoder.encode(valueString, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    PrintUtil.error(e.getMessage());
                }
            } else {
                temp.append(valueString);
            }
        }
        temp.append("&key=");
        temp.append(paySign);
        PrintUtil.info("[微信预支付] 签名参数：{}", temp.toString());
        return MD5Util.getMessageDigest(temp.toString());
    }

    /**
     * 异步通知回调统一处理
     * @param request
     * @param response
     * @return String
     */
    private String notifyHandle(HttpServletRequest request, HttpServletResponse response){
        String xml = XmlUtil.inputStreamToXmlStr(request, response);
        RefundProperties refundProperties = (RefundProperties) XmlUtil.xmlStrToBean(xml, RefundProperties.class);
        String resultCode = refundProperties.getResult_code();
        String outTradeNo = refundProperties.getOut_trade_no();
        try {
            if (resultCode.equalsIgnoreCase("FAIL")) {
                PrintUtil.error("[微信预支付] 异步通知回调，订单为{}支付失败！", outTradeNo);
                response.getWriter().write(this.setXml("SUCCESS", "OK"));
            } else if (resultCode.equalsIgnoreCase("SUCCESS")) {
                PrintUtil.error("[微信预支付] 异步通知回调，订单为{}支付成功！", outTradeNo);
                //处理订单信息以及其他相关业务逻辑
                // TODO ...
                response.getWriter().write(this.setXml("SUCCESS", "OK"));
            }
        } catch (IOException e) {
            PrintUtil.error("[微信预支付] 异步通知回调失败：{}" , e);
            throw new TransactionException("支付失败！");
        }
        return "";
    }

    /**
     * 返回微信服务
     * @param returnCode 响应的code
     * @param returnMsg 响应的message
     * @return xml结果
     */
    private String setXml(String returnCode, String returnMsg) {
        return "<xml><return_code><![CDATA[" + returnCode + "]]></return_code><return_msg><![CDATA[" + returnMsg + "]]></return_msg></xml>";
    }

    /**
     * 生成退款交易号
     * @return 退款交易号
     */
    private String generateRefundNo() {
        String refundNo = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        String str = "000000" + (int) (Math.random() * 1000000);
        return refundNo + str.substring(str.length() - 6);
    }

}
