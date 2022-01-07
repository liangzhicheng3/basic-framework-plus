package com.liangzhicheng.modules.controller;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.liangzhicheng.common.basic.BaseController;
import com.liangzhicheng.common.constant.ApiConstant;
import com.liangzhicheng.common.constant.Constants;
import com.liangzhicheng.common.oss.AssignFactory;
import com.liangzhicheng.common.push.PushFactory;
import com.liangzhicheng.common.push.service.BasePushService;
import com.liangzhicheng.common.response.ResponseResult;
import com.liangzhicheng.common.utils.*;
import com.liangzhicheng.config.aop.annotation.LogRecord;
import com.liangzhicheng.modules.entity.SysUserEntity;
import com.liangzhicheng.modules.entity.dto.SysUserDTO;
import com.liangzhicheng.modules.entity.dto.TestDTO;
import com.liangzhicheng.modules.service.ISysUserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping(value = "/test")
public class TestController extends BaseController {

    @Resource
    private ISysUserService sysUserService;

    @LogRecord(operate = "测试校验")
    @ApiOperation(value = "测试校验")
    @PostMapping(value = "/testValidate")
    public ResponseResult testValidate(@Validated @RequestBody TestDTO testDTO,
                                       BindingResult bindingResult){
        if("A".equals(testDTO.getName()) && 18 == testDTO.getAge()){
            return buildSuccessInfo();
        }
        return buildFailedInfo(ApiConstant.BASE_FAIL_CODE);
    }

    @ApiOperation(value = "测试InitBinder")
    @GetMapping(value = "/testInitBinder")
    public ResponseResult testInitBinder(String str){
        Map<String, Object> map = new HashMap<>();
        map.put("str", str);
        return buildSuccessInfo(map.toString());
    }

    @ApiOperation(value = "测试AES")
    @PostMapping(value = "/testAES")
    public ResponseResult testAES(){
        //加密传值
        String value = "userId=123&orderId=456";
        String sign = AESUtil.aesEncrypt(value);
        //接收解密
        String userId = AESUtil.getParam("userId", sign);
        String orderId = AESUtil.getParam("orderId", sign);
        Map<String, Object> map = new HashMap<>(2);
        map.put("userId", userId);
        map.put("orderId", orderId);
        return buildSuccessInfo(map);
    }

    @ApiOperation(value = "测试资源json文件获取")
    @PostMapping(value = "/testGetJson")
    public ResponseResult testGetJson(){
        List<SysUserEntity> list = ListUtil.toList("json/user.json", SysUserEntity.class);
        return buildSuccessInfo(list);
    }

    @ApiOperation(value = "测试推送")
    @PostMapping(value = "/testPush")
    public ResponseResult testPush(){
        Map<String, Object> pushMap = new HashMap<>(5);
        pushMap.put("token", null);
        pushMap.put("title", "这里是标题");
        pushMap.put("content", "这里是内容");
        pushMap.put("params", null);
        pushMap.put("appType", "IOS");
        BasePushService build = PushFactory.getInstance().build();
//        build.push(pushMap);
        build.pushAll(pushMap);
        return buildSuccessInfo();
    }

    @ApiOperation(value = "测试PDF") //html to pdf
    @PostMapping(value = "/testPDF")
    public ResponseResult testPDF(@RequestParam("jsonArray") String jsonArray){ //jsonArray:[{"srcUrl":"html文件url","name":"名称"},{...}]
        JSONArray array = JSONUtil.parseArray(jsonArray);
        JSONObject jsonObject = null;
        List<String> resultUrls = new ArrayList<>(array.size());
        for(int i = 0; i < array.size(); i++){
            jsonObject = array.getJSONObject(i);
            String srcUrl = jsonObject.getStr("srcUrl");
            String name = jsonObject.getStr("name");
            String fileName = String.format("%s#%s", name, TimeUtil.format(new Date(), "yyyy-MM-dd"));
            String filePath = String.format("%s%s.pdf", Constants.PDF_FILE_PATH, fileName);
            PDFUtil.convert(srcUrl, filePath);
            resultUrls.add(AssignFactory.getInstance().upload(filePath, fileName));
        }
        return buildSuccessInfo(resultUrls);
    }

    @ApiOperation(value = "测试自定义排序查询列表")
    @PostMapping(value = "/testUserList")
    public ResponseResult testUserList(@RequestBody SysUserDTO sysUserDTO,
                                       Pageable pageable){
        return buildSuccessInfo(sysUserService.testUserList(sysUserDTO, pageable));
    }

}
