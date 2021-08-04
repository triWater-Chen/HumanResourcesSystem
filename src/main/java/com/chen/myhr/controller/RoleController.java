package com.chen.myhr.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chen.myhr.bean.Role;
import com.chen.myhr.bean.vo.request.RolePageReq;
import com.chen.myhr.common.utils.Result;
import com.chen.myhr.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Chen
 * @since 2021-07-28
 */
@Api(tags = {"角色管理"})
@RestController
@RequestMapping("/system/basic/role")
public class RoleController {

    @Resource
    RoleService roleService;

    @ApiOperation("按条件分页查询角色")
    @GetMapping("/list")
    public Result getAllRoles(RolePageReq req) {

        // TODO: 对 PageReq 字段进行非空校验
        Page<Role> roles = roleService.listByCondition(req);
        if (ObjectUtils.isEmpty(roles.getRecords())) {
            return Result.error().message("未查询到相关角色");
        }
        return Result.done().data("list", roles).message("查询成功");
    }
}

