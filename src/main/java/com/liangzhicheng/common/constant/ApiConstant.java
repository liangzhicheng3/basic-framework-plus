package com.liangzhicheng.common.constant;

import java.util.Map;
import java.util.TreeMap;

/**
 * Api统一状态码
 * @author liangzhicheng
 */
public class ApiConstant {

	/**
	 * 存放每个状态码对应的具体信息
	 */
	public static final Map<Integer, String> map = new TreeMap<>();
	/**
	 * 成功
	 */
	public static final int BASE_SUCCESS_CODE = 10000;
	/**
	 * 失败
	 */
	public static final int BASE_FAIL_CODE = -1;
	/**
	 * 该请求是文件类型的HTTP请求
	 */
	public static final int NOT_A_FILE_REQUEST = -3;
	/**
	 * 请求过于频繁
	 */
	public static final int REQUEST_BUSY = -4;

	/**
	 * 参数不能为空
	 */
	public static final int PARAM_IS_NULL = 41001;
	/**
	 * 参数有误
	 */
	public static final int PARAM_ERROR = 41002;
	/**
	 * 参数类型有误
	 */
	public static final int PARAM_TYPE_ERROR = 41003;
	/**
	 * 手机号码参数有误
	 */
	public static final int PARAM_PHONE_ERROR = 41004;
	/**
	 * 日期参数有误
	 */
	public static final int PARAM_DATE_ERROR = 41005;
	/**
	 * 数字参数有误
	 */
	public static final int PARAM_NUMBER_ERROR = 41006;
	/**
	 * 小数参数有误
	 */
	public static final int PARAM_DOUBLE_ERROR = 41007;
	/**
	 * json参数格式或类型有误
	 */
	public static final int PARAM_JSON_ERROR = 41008;
	/**
	 * 参数格式有误
	 */
	public static final int PARAM_FORMAT_ERROR = 41009;
	/**
	 * 验证码有误
	 */
	public static final int PARAM_VCODE_ERROR = 41010;
	/**
	 * 邮箱格式有误
	 */
	public static final int PARAM_EMAIL_ERROR = 41011;

	/**
	 * 未认证
	 */
	public static final int NO_AUTHENCATION = 41201;
	/**
	 * 权限不足
	 */
	public static final int NO_AUTHORIZATION = 41202;

	/**
	 * 用户不存在
	 */
	public static final int USER_NOT_EXIST = 43001;
	/**
	 * 用户已存在
	 */
	public static final int USER_EXIST = 43002;
	/**
	 * 用户已被停用
	 */
	public static final int USER_IS_LOCKED = 43003;

	/**
	 * 未登录
	 */
	public static final int NO_LOGIN = 42003;
	/**
	 * 密码错误
	 */
	public static final int PASSWORD_ERROR = 42004;

	static{
		map.put(BASE_SUCCESS_CODE, "success");
		map.put(BASE_FAIL_CODE, "fail");
		map.put(NOT_A_FILE_REQUEST, "该请求是文件类型的HTTP请求");
		map.put(REQUEST_BUSY, "请求过于频繁");

		map.put(PARAM_IS_NULL, "参数不能为空");
		map.put(PARAM_ERROR, "参数有误");
		map.put(PARAM_TYPE_ERROR, "参数类型有误");
		map.put(PARAM_PHONE_ERROR, "手机号码参数有误");
		map.put(PARAM_DATE_ERROR, "日期参数有误");
		map.put(PARAM_NUMBER_ERROR, "数字参数有误");
		map.put(PARAM_DOUBLE_ERROR, "小数参数有误");
		map.put(PARAM_JSON_ERROR, "json参数格式或类型有误");
		map.put(PARAM_FORMAT_ERROR, "参数格式有误");
		map.put(PARAM_VCODE_ERROR, "验证码有误");
		map.put(PARAM_EMAIL_ERROR, "邮箱格式有误");

		map.put(NO_AUTHENCATION, "未认证");
		map.put(NO_AUTHORIZATION, "权限不足");

		map.put(USER_NOT_EXIST, "用户不存在");
		map.put(USER_EXIST, "用户已存在");
		map.put(USER_IS_LOCKED, "用户已被停用");

		map.put(NO_LOGIN, "未登录");
		map.put(PASSWORD_ERROR, "密码错误");
	}

	public static String getMessage(int errorCode){
		return map.get(errorCode);
	}

}
