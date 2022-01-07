package com.liangzhicheng.modules.controller;

import com.liangzhicheng.common.basic.BaseController;
import com.liangzhicheng.common.oss.OSSFactory;
import com.liangzhicheng.common.oss.service.BaseCloudStorageService;
import com.liangzhicheng.common.response.ResponseResult;
import com.liangzhicheng.common.utils.AssertUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Api(value="FileController", tags={"文件相关控制器"})
@RestController
@RequestMapping(value = "/file")
public class FileController extends BaseController {

    @ApiOperation(value = "文件上传")
    @PostMapping(value = "/upload")
    public ResponseResult upload(@RequestParam("file") MultipartFile file) throws Exception {
        AssertUtil.isFalse(file.isEmpty(), "文件不能为空");
        BaseCloudStorageService build = OSSFactory.getInstance().build();
        String url = build.upload(file);
        return buildSuccessInfo(url);
    }

}
