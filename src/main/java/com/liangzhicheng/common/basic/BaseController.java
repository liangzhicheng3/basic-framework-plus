package com.liangzhicheng.common.basic;

import com.liangzhicheng.common.constant.ApiConstant;
import com.liangzhicheng.common.response.ResponseResult;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

/**
 * 接口请求结果信息返回基础控制器
 * @author liangzhicheng
 */
public abstract class BaseController {

    /**
     * 初始化请求参数
     * @param binder
     */
    @InitBinder
    protected void initRequestParams(WebDataBinder binder){
        //将字符串自动trim去掉前后空格
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(false));
    }

    protected ResponseResult buildSuccessInfo() {
        return this.buildSuccessInfo(null);
    }

    protected ResponseResult buildSuccessInfo(Object resultData) {
        return new ResponseResult(ApiConstant.BASE_SUCCESS_CODE, ApiConstant.getMessage(ApiConstant.BASE_SUCCESS_CODE), resultData);
    }

    protected ResponseResult buildSuccessInfo(int successCode) {
        return new ResponseResult(ApiConstant.BASE_SUCCESS_CODE, ApiConstant.getMessage(successCode));
    }

    protected ResponseResult buildFailedInfo(int errorCode) {
        return new ResponseResult(errorCode, ApiConstant.getMessage(errorCode), null);
    }

    protected ResponseResult buildFailedInfo(String errorMsg) {
        return new ResponseResult(ApiConstant.BASE_FAIL_CODE, errorMsg, null);
    }

    protected ResponseResult buildFailedInfo(int errorCode, String appendMsg) {
        return new ResponseResult(errorCode, appendMsg, null);
    }

}
