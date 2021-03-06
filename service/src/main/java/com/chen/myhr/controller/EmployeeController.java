package com.chen.myhr.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chen.myhr.bean.*;
import com.chen.myhr.bean.vo.request.EmployeePageReq;
import com.chen.myhr.bean.vo.result.DepartmentWithChildren;
import com.chen.config.result.Result;
import com.chen.utils.poi.ExcelUtil;
import com.chen.myhr.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

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

    @Resource
    JoblevelService joblevelService;

    @Resource
    DepartmentService departmentService;

    @Resource
    PoliticsstatusService politicsstatusService;

    @Resource
    NationService nationService;

    @Resource
    PositionService positionService;

    @ApiOperation("按条件分页查询员工")
    @GetMapping("/list")
    public Result getEmployee(@Valid EmployeePageReq req) {

        Page<Employee> employees = employeeService.listByCondition(req);
        if (ObjectUtils.isEmpty(employees.getRecords())) {
            return Result.done().code(500).message("未查询到相关员工");
        }
        return Result.done().data("list", employees).message("查询成功");
    }

    @ApiOperation("查询职称列表")
    @GetMapping("/jobLevels")
    public Result getListOfJobLevels() {

        List<Joblevel> jobLevels = joblevelService.getBaseMapper().selectList(
                new QueryWrapper<Joblevel>().select("id", "name", "enabled")
        );
        return Result.done().data("jobLevels", jobLevels);
    }

    @ApiOperation("查询政治面貌列表")
    @GetMapping("/politicsStatus")
    public Result getListOfPoliticsStatus() {

        List<Politicsstatus> politicsStatus = politicsstatusService.list();
        return Result.done().data("politicsStatus", politicsStatus);
    }

    @ApiOperation("查询民族列表")
    @GetMapping("/nations")
    public Result getListOfNations() {

        List<Nation> nations = nationService.list();
        return Result.done().data("nations", nations);
    }

    @ApiOperation("查询职位列表")
    @GetMapping("/positions")
    public Result getListOfPositions() {

        List<Position> positions = positionService.getBaseMapper().selectList(
                new QueryWrapper<Position>().select("id", "name", "enabled")
        );
        return Result.done().data("positions", positions);
    }

    @ApiOperation("查询部门列表")
    @GetMapping("/departments")
    public Result getListOfDepartments() {

        List<DepartmentWithChildren> departments = departmentService.getDepartmentsTree();
        return Result.done().data("departments", departments);
    }

    @ApiOperation("添加员工")
    @PostMapping("/add")
    public Result addEmployee(@Valid @RequestBody Employee employee) {

        if (employeeService.checkEmployeeIdCard(employee)) {
            return Result.error().message("添加失败，该身份证号已存在，请核对后再添加");
        }

        if (employeeService.addEmployee(employee)) {
            return Result.done().message("添加成功");
        }
        return Result.error().message("添加失败");
    }

    @ApiOperation("修改员工")
    @PostMapping("/update")
    public Result updateEmployee(@Valid @RequestBody Employee employee) {

        if (employeeService.checkEmployeeIdCard(employee)) {
            return Result.error().message("修改失败，该身份证号已存在，请核对后再添加");
        }

        if (employeeService.updateEmployee(employee)) {
            return Result.done().message("修改成功");
        }
        return Result.error().message("修改失败");
    }

    @ApiOperation("批量删除员工")
    @PostMapping("/removeBatch")
    public Result removeBatchEmployee(@RequestBody List<Integer> ids) {

        if (employeeService.removeByIds(ids)) {
            return Result.done().message("删除成功");
        }
        return Result.error().message("删除失败");
    }

    @ApiOperation("按条件导出员工")
    @GetMapping("/export")
    public Result exportEmployee(EmployeePageReq req) {

        List<Employee> employees = employeeService.listWithoutPage(req);
        ExcelUtil<Employee> excelUtil = new ExcelUtil<>(Employee.class);

        return excelUtil.exportExcel(employees, "员工详情");
    }

    @ApiOperation("导入员工")
    @PostMapping("/import")
    public Result importEmployee(MultipartFile file, boolean updateSupport) throws Exception {

        ExcelUtil<Employee> excelUtil = new ExcelUtil<>(Employee.class);
        List<Employee> employees = excelUtil.importExcel(file.getInputStream());

        String message = employeeService.importEmployees(employees, updateSupport);
        String failure = "很抱歉，导入失败！";
        if (message.contains(failure)) {
            return Result.error().message(message);
        }
        return Result.done().message(message);
    }
}

