package com.liangzhicheng.modules.controller;

import com.liangzhicheng.common.basic.BaseController;
import com.liangzhicheng.common.constant.ApiConstant;
import com.liangzhicheng.common.response.ResponseResult;
import com.liangzhicheng.common.utils.ToolUtil;
import com.liangzhicheng.modules.service.IAlipayService;
import com.liangzhicheng.modules.service.IWeChatPayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Api(value="PayController", tags={"支付相关控制器"})
@RestController
@RequestMapping("/pay")
public class PayController extends BaseController {

    @Resource
    private IWeChatPayService weChatPayService;
    @Resource
    private IAlipayService alipayService;

    @ApiOperation(value = "微信预支付-APP")
    @PostMapping(value = "/wc/app/prepay")
    public ResponseResult prepayWeChatAPP(String orderId){
        Map<Object, Object> resultMap = weChatPayService.prepayWeChatAPP(orderId);
        if(ToolUtil.isNotNull(resultMap)){
            return buildSuccessInfo(resultMap);
        }
        return buildFailedInfo("支付失败！");
    }

    @ApiOperation(value = "微信预支付-小程序")
    @PostMapping(value = "/wc/mini/prepay")
    public ResponseResult prepayWeChatMINI(String orderId){
        Map<Object, Object> resultMap = weChatPayService.prepayWeChatMINI(orderId);
        if(ToolUtil.isNotNull(resultMap)){
            return buildSuccessInfo(resultMap);
        }
        return buildFailedInfo("支付失败！");
    }

    @ApiOperation(value = "支付宝预支付-APP")
    @PostMapping(value = "/alipay/app/prepay")
    public ResponseResult prepayAlipayAPP(String orderId){
        return buildSuccessInfo(alipayService.prepayAlipayAPP(orderId));
    }

    @ApiOperation(value = "支付宝预支付-网页")
    @PostMapping(value = "/alipay/web/prepay")
    public ResponseResult prepayAlipayWEB(String orderId,
                                          HttpServletResponse response){
        alipayService.prepayAlipayWEB(orderId, response);
        return buildSuccessInfo();
    }

    @ApiOperation(value = "微信支付退款")
    @PostMapping(value = "/wc/refund")
    public ResponseResult refundWeChat(String orderId){
        String result = weChatPayService.refundWeChat(orderId);
        if("success".equals(result)){
            return buildSuccessInfo("退款成功！");
        }
        return buildFailedInfo("退款失败！");
    }

    @ApiOperation(value = "支付宝退款")
    @PostMapping(value = "/alipay/refund")
    public ResponseResult refundAlipay(String orderId){
        String result = alipayService.refundAlipay(orderId);
        if("success".equals(result)){
            return buildSuccessInfo("退款成功！");
        }
        return buildFailedInfo("退款失败！");
    }


    @ApiIgnore
    @RequestMapping(value = "/wc/app/notify_url")
    public ResponseResult notifyUrlAPP(HttpServletRequest request,
                                       HttpServletResponse response){
        return buildSuccessInfo(weChatPayService.notifyUrlAPP(request, response));
    }

    @ApiIgnore
    @RequestMapping(value = "/wc/mini/notify_url")
    public ResponseResult notifyUrlMINI(HttpServletRequest request,
                                        HttpServletResponse response){
        return buildSuccessInfo(weChatPayService.notifyUrlMINI(request, response));
    }

    @ApiIgnore
    @RequestMapping(value = "/alipay/app/notify_url")
    public ResponseResult notifyUrlAPP(@RequestParam HashMap<String, String> params){
        String result = alipayService.notifyUrlAPP(params);
        if("success".equals(result)){
            return buildSuccessInfo();
        }
        return buildFailedInfo(ApiConstant.BASE_FAIL_CODE);
    }

    @ApiIgnore
    @RequestMapping(value = "/alipay/web/notify_url")
    public ResponseResult notifyUrlWEB(@RequestParam HashMap<String, String> params){
        String result = alipayService.notifyUrlWEB(params);
        if("success".equals(result)){
            return buildSuccessInfo();
        }
        return buildFailedInfo(ApiConstant.BASE_FAIL_CODE);
    }

}
