package com.liangzhicheng.modules.controller;

import com.liangzhicheng.common.basic.BaseController;
import com.liangzhicheng.common.constant.ApiConstant;
import com.liangzhicheng.common.response.ResponseResult;
import com.liangzhicheng.modules.entity.dto.AreaDTO;
import com.liangzhicheng.modules.entity.vo.AreaVO;
import com.liangzhicheng.modules.service.IAreaNameService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api(value="AreaController", tags={"地区相关控制器"})
@RestController
@RequestMapping("/area")
public class AreaController extends BaseController {

    @Resource
    private IAreaNameService areaNameService;

    @ApiOperation(value = "地区列表")
    @PostMapping(value = "/list")
    @ApiResponses({@ApiResponse(code = ApiConstant.BASE_SUCCESS_CODE,
            message = "成功", response = AreaVO.class)})
    public ResponseResult list(@Validated @RequestBody AreaDTO areaDTO,
                               Pageable pageable,
                               BindingResult bindingResult){
        return buildSuccessInfo(areaNameService.listArea(areaDTO, pageable));
    }

}
