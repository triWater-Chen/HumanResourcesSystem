package com.chen.myhr.controller;

import com.chen.myhr.bean.Hr;
import com.chen.myhr.bean.vo.request.HrReq;
import com.chen.myhr.common.utils.Result;
import com.chen.myhr.service.HrService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
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

    @ApiOperation("修改用户")
    @PostMapping("/update")
    public Result updateHr(@Valid @RequestBody Hr hr) {

        // ----- 只有超级管理员才能修改超级管理员 -----
            // 获取当前登录角色
            Integer id = ((Hr) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
            int adminId = 3;

            // 当前登录用户为非超级管理员 并 对管理员进行操作
            if (id != adminId && hr.getId() == adminId) {
                return Result.error().message("当前用户无权修改【超级管理员】");
            }

            // 不允许修改超级管理员的状态
            if (hr.getId() == adminId && !hr.isEnabled()) {
                return Result.error().message("【超级管理员】的状态无法修改");
            }

        // 检查用户名和手机号是否重复
        if (hrService.checkHrUsernameAndPhone(hr)) {
            return Result.error().message("修改失败，此用户名或手机号已存在");
        }

        // 若 是超级管理员 或 对非超级管理员进行操作，则运行进行
        if (hrService.updateById(hr)) {
            return Result.done().message("修改成功");
        } else {
            return Result.error().message("修改失败");
        }
    }
}

