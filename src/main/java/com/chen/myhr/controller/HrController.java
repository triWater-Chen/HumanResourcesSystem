package com.chen.myhr.controller;

import com.chen.myhr.bean.Hr;
import com.chen.myhr.bean.vo.request.HrReq;
import com.chen.myhr.common.utils.Result;
import com.chen.myhr.service.HrService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Chen
 * @since 2021-07-28
 */
@Api(tags = {"用户管理"})
@RestController
@RequestMapping("/system/hr")
public class HrController {

    @Resource
    HrService hrService;

    @ApiOperation("按条件查询用户（不返回密码）")
    @GetMapping("/list")
    public Result getAllHrsWithRole(HrReq req) {

        List<Hr> hrs = hrService.listHrByCondition(req);
        if (ObjectUtils.isEmpty(hrs)) {
            return Result.done().code(500).message("未查询到相关用户");
        }
        return Result.done().data("list", hrs).message("查询成功");
    }
}

