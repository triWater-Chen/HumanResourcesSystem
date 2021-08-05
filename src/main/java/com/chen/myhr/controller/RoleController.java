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
    public Result getAllRoles(RolePageReq req) {

        // TODO: 对 PageReq 字段进行非空校验
        Page<Role> roles = roleService.listByCondition(req);
        if (ObjectUtils.isEmpty(roles.getRecords())) {
            return Result.error().message("未查询到相关角色");
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

    @ApiOperation("添加/修改角色")
    @PostMapping("/update")
    public Result updateRole(@RequestBody RoleUpdateReq req) {

        // 对角色的菜单权限进行添加/修改
        boolean result =  menuRoleService.updateMenuByRole(req);

        // 对角色的基本信息进行添加/修改
        Role role = new Role();
        role.setId(req.getId());
        role.setName("ROLE_" + req.getName());
        role.setNamezh(req.getNamezh());
        role.setEnabled(req.getEnabled());
        boolean update = roleService.saveOrUpdate(role);

        if (result && update) {
            return Result.done().message("更新成功");
        } else {
            return Result.error().message("更新失败");
        }
    }
}

