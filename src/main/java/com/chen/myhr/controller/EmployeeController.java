package com.chen.myhr.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chen.myhr.bean.Employee;
import com.chen.myhr.bean.vo.request.EmployeePageReq;
import com.chen.myhr.common.utils.Result;
import com.chen.myhr.service.EmployeeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author Chen
 * @since 2021-07-28
 */
@Api(tags = {"员工详情"})
@RestController
@RequestMapping("/personnel/emp")
public class EmployeeController {

    @Resource
    EmployeeService employeeService;

    @ApiOperation("按条件分页查询员工")
    @GetMapping("/list")
    public Result getEmployee(@Valid EmployeePageReq req) {

        Page<Employee> employees = employeeService.listByCondition(req);
        if (ObjectUtils.isEmpty(employees.getRecords())) {
            return Result.done().code(500).message("未查询到相关员工");
        }
        return Result.done().data("list", employees).message("查询成功");
    }
}

