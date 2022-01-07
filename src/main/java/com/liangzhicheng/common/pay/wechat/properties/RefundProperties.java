package com.liangzhicheng.common.pay.wechat.properties;

import lombok.Data;

import java.io.Serializable;

/**
 * 微信退款实体类
 * @author liangzhicheng
 */
@Data
public class RefundProperties implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 返回状态码
	 */
	private String return_code;

	/**
	 * 返回信息
	 */
	private String return_msg;

	//以下字段在return_code为SUCCESS的时候有返回
	/**
	 * 业务结果
	 */
	private String result_code;

	/**
	 * 错误代码
	 */
	private String err_code;

	/**
	 * 错误代码描述
	 */
	private String err_code_des;

	/**
	 * 小程序id
	 */
	private String appid;

	/**
	 * 商户号
	 */
	private String mch_id;

	/**
	 * 随机字符串
	 */
	private String nonce_str;

	/**
	 * 签名
	 */
	private String sign;

	/**
	 * 微信支付订单号
	 */
	private String transaction_id;

	/**
	 * 商户订单号
	 */
	private String out_trade_no;

	/**
	 * 商户退款单号
	 */
	private String out_refund_no;

	/**
	 * 微信支付退款单号
	 */
	private String refund_id;

	/**
	 * 退款金额
	 */
	private String refund_fee;

	/**
	 * 应结退款金额
	 */
	private String settlement_refund_fee;

	/**
	 * 标价金额
	 */
	private String total_fee;

	/**
	 * 应结订单金额
	 */
	private String settlement_total_fee;

	/**
	 * 标价币种：默认CNY
	 */
	private String fee_type;

	/**
	 * 现金支付金额
	 */
	private String cash_fee;

	/**
	 * 现金退款金额
	 */
	private String cash_refund_fee;

}
