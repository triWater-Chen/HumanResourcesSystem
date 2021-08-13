package com.chen.myhr.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.chen.myhr.bean.Hr;
import com.chen.myhr.bean.vo.request.HrReq;
import com.chen.myhr.common.utils.CommonConstants;
import com.chen.myhr.common.utils.Result;
import com.chen.myhr.service.HrService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

    /**
     * 对应超级管理员的 id
     */
    int adminId = 3;

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

    @ApiOperation("添加用户")
    @PostMapping("/add")
    public Result addHr(@Valid @RequestBody Hr hr) {

        // 检查用户名和手机号是否重复
        if (hrService.checkHrUsernameAndPhone(hr)) {
            return Result.error().message("添加失败，此用户名或手机号已存在");
        }

        // 将密码进行加密
        String encode = new BCryptPasswordEncoder().encode(hr.getPassword());
        hr.setPassword(encode);

        // 自动填充默认头像
        hr.setUserFace(CommonConstants.AVATAR);

        // 添加用户
        if (hrService.save(hr)) {
            return Result.done().message("添加成功");
        }
        return Result.error().message("添加失败");
    }

    @ApiOperation("修改用户")
    @PostMapping("/update")
    public Result updateHr(@Valid @RequestBody Hr hr) {

        // ----- 只有超级管理员才能修改超级管理员 -----
            // 获取当前登录用户 id
            Integer id = ((Hr) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();

            // 当前登录用户为非超级管理员 并 对管理员进行操作
            if (id != adminId && hr.getId() == adminId) {
                return Result.error().message("您无权修改【超级管理员】");
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

    @ApiOperation("删除用户")
    @PostMapping("/remove/{id}")
    public Result removeHr(@PathVariable Integer id) {

        // 不允许删除超级管理员
        if (id == adminId) {
            return Result.error().message("【超级管理员】无法删除！");
        }

        // 将同时删除用户与角色的关联
        if (hrService.removeHr(id)) {
            return Result.done().message("删除成功");
        } else {
            return Result.error().message("删除失败");
        }
    }

    @ApiOperation("管理员直接重置用户密码（不需要旧密码）")
    @PostMapping("/resetPassword")
    public Result resetHrPassword(@RequestBody Hr hr) {

        // ----- 超级管理员的密码只有其自己才能直接重置 -----
            // 判断当前登录用户是否是超级管理员
            Integer id = ((Hr) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
            if (id != adminId && hr.getId() == adminId) {
                return Result.error().message("您无权直接重置【超级管理员】的密码");
            }

        // 根据 id 只更新密码字段
        String encode = new BCryptPasswordEncoder().encode(hr.getPassword());
        boolean reset = hrService.update(new UpdateWrapper<Hr>().eq("id", hr.getId()).set("password", encode));

        if (reset) {
            return Result.done().message("重置成功");
        } else {
            return Result.error().message("重置失败");
        }
    }
}

