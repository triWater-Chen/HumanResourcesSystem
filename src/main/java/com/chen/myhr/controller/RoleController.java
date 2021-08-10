package com.chen.myhr.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chen.myhr.bean.MenuRole;
import com.chen.myhr.bean.Role;
import com.chen.myhr.bean.vo.request.RolePageReq;
import com.chen.myhr.bean.vo.request.RoleUpdateReq;
import com.chen.myhr.bean.vo.result.MenuWithChildren;
import com.chen.myhr.common.utils.Result;
import com.chen.myhr.service.MenuRoleService;
import com.chen.myhr.service.MenuService;
import com.chen.myhr.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
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

    @Resource
    MenuService menuService;

    @Resource
    MenuRoleService menuRoleService;

    @ApiOperation("按条件分页查询角色")
    @GetMapping("/list")
    public Result getRoles(@Valid RolePageReq req) {

        Page<Role> roles = roleService.listByCondition(req);
        if (ObjectUtils.isEmpty(roles.getRecords())) {
            return Result.done().code(500).message("未查询到相关角色");
        }
        return Result.done().data("list", roles).message("查询成功");
    }

    @ApiOperation("返回树形菜单")
    @GetMapping("/menuTree")
    public Result getMenuTree() {

        List<MenuWithChildren> menuTree = menuService.getMenusWithChildren();
        return Result.done().data("menus", menuTree);
    }

    @ApiOperation("根据角色 id 查询对应的菜单 id")
    @GetMapping("/menuByRole/{id}")
    public Result getMenuByRole(@PathVariable int id) {

        QueryWrapper<MenuRole> menuRoleQueryWrapper = new QueryWrapper<>();
        menuRoleQueryWrapper.eq("rid", id);
        List<MenuRole> menuRoleList = menuRoleService.list(menuRoleQueryWrapper);

        // 将查询出的 menu id 放入列表中
        List<Integer> menuIdByRole = new ArrayList<>();
        for (MenuRole menuRole : menuRoleList) {
            menuIdByRole.add(menuRole.getMid());
        }

        return Result.done().data("list", menuIdByRole);
    }

    @ApiOperation("修改角色状态")
    @PostMapping("/changeStatus")
    public Result changeStatus(@Valid @RequestBody Role role) {

        if (roleService.updateById(role)) {
            return Result.done();
        } else {
            return Result.error();
        }
    }

    @ApiOperation("添加/修改角色")
    @PostMapping("/saveOrUpdate")
    public Result updateRole(@Valid @RequestBody RoleUpdateReq req) {

        // 此处使用数据库来判断字段重复（因为查询名称没排除被修改该行数据）

        if (roleService.saveOrUpdateRoleWithMenu(req)) {
            return Result.done().message("更新成功");
        } else {
            return Result.error().message("更新失败");
        }
    }

    @ApiOperation("批量删除角色（将会删除 menu_role 和 hr_role 相关数据")
    @PostMapping("/removeBatch")
    public Result removeBatchRole(@RequestBody List<Integer> ids) {

        if (roleService.removeBatchRoleWithMenu(ids)) {
            return Result.done().message("删除成功");
        } else {
            return Result.error().message("删除失败");
        }
    }
}

