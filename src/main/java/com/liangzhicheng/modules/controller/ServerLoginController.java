package com.liangzhicheng.modules.controller;

import com.liangzhicheng.common.basic.BaseController;
import com.liangzhicheng.common.constant.ApiConstant;
import com.liangzhicheng.common.response.ResponseResult;
import com.liangzhicheng.config.mvc.interceptor.annotation.LoginValidate;
import com.liangzhicheng.modules.entity.dto.SysUserDTO;
import com.liangzhicheng.modules.entity.vo.SysUserLoginVO;
import com.liangzhicheng.modules.service.ISysUserService;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Api(value="ServerLoginController", tags={"登录相关控制器"})
@RestController
@RequestMapping(value = "/server")
public class ServerLoginController extends BaseController {

    @Resource
    private ISysUserService sysUserService;

    @ApiOperation(value = "登录")
    @PostMapping(value = "/login")
    @ApiOperationSupport(ignoreParameters = {"userDTO.companyId", "userDTO.deptId",
            "userDTO.roleIds", "userDTO.keyword", "userDTO.id", "userDTO.truename",
            "userDTO.avatar", "userDTO.isAdmin", "userDTO.loginStatus",
            "userDTO.newPassword", "userDTO.page", "userDTO.pageSize"})
    @ApiResponses({@ApiResponse(code = ApiConstant.BASE_SUCCESS_CODE,
            message = "成功", response = SysUserLoginVO.class)})
    public ResponseResult login(@RequestBody SysUserDTO userDTO,
                                HttpServletRequest request){
        return buildSuccessInfo(sysUserService.login(userDTO, request));
    }

    @LoginValidate
    @ApiOperation(value = "退出登录")
    @PostMapping(value = "/logOut")
    public ResponseResult logOut(HttpServletRequest request){
        sysUserService.logOut(request);
        return buildSuccessInfo();
    }

}

