package com.chen.myhr.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chen.config.result.Result;
import com.chen.myhr.bean.Empsalary;
import com.chen.myhr.bean.Salary;
import com.chen.myhr.bean.vo.request.EmployeePageReq;
import com.chen.myhr.bean.vo.result.DepartmentWithChildren;
import com.chen.myhr.bean.vo.result.EmployeeWithSalaryPage;
import com.chen.myhr.service.DepartmentService;
import com.chen.myhr.service.EmpsalaryService;
import com.chen.myhr.service.SalaryService;
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
@Api(tags = {"员工账套管理"})
@RestController
@RequestMapping("/salary/sobConfig")
public class EmpSalaryController {

    @Resource
    EmpsalaryService empsalaryService;

    @Resource
    SalaryService salaryService;

    @Resource
    DepartmentService departmentService;

    @ApiOperation("按条件查询员工账套")
    @GetMapping("/list")
    public Result getEmpList(@Valid EmployeePageReq req) {

        EmployeeWithSalaryPage employees = empsalaryService.getEmployeeList(req);
        if (ObjectUtils.isEmpty(employees.getList())) {
            return Result.done().code(500).message("未查询到相关员工");
        }
        return Result.done().data("salaries", employees).message("查询成功");
    }

    @ApiOperation("查询部门列表")
    @GetMapping("/departments")
    public Result getListOfDepartments() {

        List<DepartmentWithChildren> departments = departmentService.getDepartmentsTree();
        return Result.done().data("departments", departments);
    }

    @ApiOperation("查询工资账套列表")
    @GetMapping("/sobs")
    public Result getListOfSobs() {

        List<Salary> sobs = salaryService.getBaseMapper().selectList(new QueryWrapper<Salary>().select("id", "name"));
        return Result.done().data("sobs", sobs);
    }

    @ApiOperation("修改员工工资账套")
    @PostMapping("/update")
    public Result updateEmpSalary(@Valid @RequestBody Empsalary empsalary) {

        if (empsalaryService.updateEmpSalary(empsalary)) {
            return Result.done().message("修改成功");
        }
        return Result.error().message("修改失败");
    }
}

