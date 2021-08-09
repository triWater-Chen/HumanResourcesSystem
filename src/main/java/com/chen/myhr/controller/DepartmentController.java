package com.chen.myhr.controller;

import com.chen.myhr.bean.Department;
import com.chen.myhr.bean.vo.request.DepartmentReq;
import com.chen.myhr.bean.vo.result.DepartmentWithChildren;
import com.chen.myhr.common.utils.CommonConstants;
import com.chen.myhr.common.utils.Result;
import com.chen.myhr.service.DepartmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @author Chen
 * @since 2021-07-28
 */
@Api(tags = {"部门管理"})
@RestController
@RequestMapping("/system/basic/department")
public class DepartmentController {

    @Resource
    DepartmentService departmentService;

    @ApiOperation("查询部门树")
    @GetMapping("/departmentTree")
    public Result getDepartmentsTree() {
        // 如果部门数量很多，递推会很慢，此时改为前端使用懒加载实现

        List<DepartmentWithChildren> departmentTree = departmentService.getDepartmentsTree();
        return Result.done().data("tree", departmentTree);
    }

    @ApiOperation("按条件查询部门")
    @GetMapping("/list")
    public Result getDepartments(DepartmentReq req) {

        List<Department> departments = departmentService.listByCondition(req);
        if (ObjectUtils.isEmpty(departments)) {
            return Result.error().code(200).message("未查询到相关角色");
        }
        return Result.done().data("list", departments).message("查询成功");
    }

    @ApiOperation("添加部门")
    @PostMapping("/add")
    public Result addDepartment(@Valid @RequestBody Department req) {

        if (departmentService.save(req)) {
            return Result.done().message("添加成功");
        }
        return Result.error().message("添加失败");
    }

    @ApiOperation("修改部门")
    @PostMapping("/update")
    public Result updateDepartment(@Valid @RequestBody DepartmentWithChildren req) {

        if (req.getParentId().equals(req.getId())) {
            return Result.error().message("操作失败，上级部门不能是自己");
        }

        String result = departmentService.updateDepartment(req);
        if (CommonConstants.STATUS.equals(result)) {
            return Result.error().message("操作失败，该部门中包含未停用的子部门");
        }
        if (CommonConstants.SQL_SUCCESS.equals(result)) {
            return Result.done().message("更新成功");
        }
        return Result.error().message("更新失败");
    }
}

