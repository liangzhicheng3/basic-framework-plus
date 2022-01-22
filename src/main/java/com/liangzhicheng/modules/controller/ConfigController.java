package com.liangzhicheng.modules.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.liangzhicheng.common.basic.BaseController;
import com.liangzhicheng.common.constant.ApiConstant;
import com.liangzhicheng.common.constant.Constants;
import com.liangzhicheng.common.oss.group.AliCloudGroup;
import com.liangzhicheng.common.oss.group.QiniuCloudGroup;
import com.liangzhicheng.common.oss.group.ServerGroup;
import com.liangzhicheng.common.oss.group.TencentCloudGroup;
import com.liangzhicheng.common.oss.properties.CloudStorage;
import com.liangzhicheng.common.push.group.AliPushGroup;
import com.liangzhicheng.common.push.group.XingePushGroup;
import com.liangzhicheng.common.push.properties.Push;
import com.liangzhicheng.common.response.ResponseResult;
import com.liangzhicheng.common.utils.JSONUtil;
import com.liangzhicheng.common.utils.ValidateUtil;
import com.liangzhicheng.common.utils.WeChatUtil;
import com.liangzhicheng.modules.entity.SysConfigEntity;
import com.liangzhicheng.modules.service.ISysConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

@Api(value="ConfigController", tags={"配置相关控制器"})
@RestController
@RequestMapping("/config")
public class ConfigController extends BaseController {

    @Resource
    private ISysConfigService configService;

    @ApiOperation(value = "保存云存储配置")
    @PostMapping(value = "/saveCloud")
    public ResponseResult save(@RequestBody CloudStorage cloudStorage){
        ValidateUtil.getInstance().validate(cloudStorage);
        switch(cloudStorage.getType()){
            case 1:
                ValidateUtil.getInstance().validate(cloudStorage, QiniuCloudGroup.class);
                break;
            case 2:
                ValidateUtil.getInstance().validate(cloudStorage, AliCloudGroup.class);
                break;
            case 3:
                ValidateUtil.getInstance().validate(cloudStorage, TencentCloudGroup.class);
                break;
            case 4:
                ValidateUtil.getInstance().validate(cloudStorage, ServerGroup.class);
                break;
        }
        configService.lambdaUpdate()
                .eq(SysConfigEntity::getKeyName, Constants.CLOUD_STORAGE_CONFIG_KEY)
                .set(SysConfigEntity::getValue, JSONUtil.toJSONString(cloudStorage))
                .update();
        return buildSuccessInfo();
    }

    @ApiOperation(value = "保存推送配置")
    @PostMapping(value = "/savePush")
    public ResponseResult save(@RequestBody Push push){
        ValidateUtil.getInstance().validate(push);
        switch(push.getType()){
            case 1:
                ValidateUtil.getInstance().validate(push, XingePushGroup.class);
                break;
            case 2:
                ValidateUtil.getInstance().validate(push, AliPushGroup.class);
                break;
        }
        configService.lambdaUpdate()
                .eq(SysConfigEntity::getKeyName, Constants.PUSH_CONFIG_KEY)
                .set(SysConfigEntity::getValue, JSONUtil.toJSONString(push))
                .update();
        return buildSuccessInfo();
    }

    @ApiOperation(value = "获取配置")
    @GetMapping(value = "/get/{type}")
    public ResponseResult get(@PathVariable("type") Integer type){
        LambdaQueryWrapper<SysConfigEntity> wrapperConfig = Wrappers.<SysConfigEntity>lambdaQuery();
        switch(type){
            case 1:
                wrapperConfig.eq(SysConfigEntity::getKeyName, Constants.CLOUD_STORAGE_CONFIG_KEY);
                break;
            case 2:
                wrapperConfig.eq(SysConfigEntity::getKeyName, Constants.PUSH_CONFIG_KEY);
                break;
        }
        SysConfigEntity config = configService.getOne(wrapperConfig);
        switch(type){
            case 1:
                return buildSuccessInfo(JSONUtil.parseObject(config.getValue(), CloudStorage.class));
            case 2:
                return buildSuccessInfo(JSONUtil.parseObject(config.getValue(), Push.class));
        }
        return buildFailedInfo(ApiConstant.BASE_FAIL_CODE);
    }

    @ApiOperation(value = "获取js-sdk参数")
    @GetMapping(value = "/getJs/{url}")
    public ResponseResult getJs(@PathVariable("url") String url) {
        Long timestamp = new Date().getTime() / 1000;
        String nonceStr = UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(16);
        SortedMap<String, Object> map = new TreeMap<>();
        map.put("noncestr", nonceStr);
        map.put("jsapi_ticket", WeChatUtil.getJsapiTicket());
        map.put("timestamp", timestamp);
        map.put("url", url);
        map.put("signature", WeChatUtil.signSHA1(map));
        map.put("appId", "公众号appid");
        return buildSuccessInfo(map);
    }

}
