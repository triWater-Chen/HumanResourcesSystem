package com.chen.myhr.controller;


import com.chen.myhr.bean.vo.result.DepartmentWithChildren;
import com.chen.myhr.common.utils.Result;
import com.chen.myhr.service.DepartmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
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
    @GetMapping("/list")
    public Result getDepartmentsTree() {
        // 如果部门数量很多，递推会很慢，此时改为前端使用懒加载实现

        List<DepartmentWithChildren> departmentTree = departmentService.getDepartmentsTree();
        return Result.done().data("list", departmentTree);
    }
}

