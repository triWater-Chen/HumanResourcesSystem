package com.chen.myhr.controller;

import com.chen.config.result.Result;
import com.chen.myhr.bean.Salary;
import com.chen.myhr.service.SalaryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @author Chen
 * @since 2021-07-28
 */
@Api(tags = {"工资账套管理"})
@RestController
@RequestMapping("/salary/sob")
public class SalaryController {

    @Resource
    SalaryService salaryService;

    @ApiOperation("获取工资账套")
    @GetMapping("/list")
    public Result getAllSalarySobs() {

        List<Salary> sobs = salaryService.list();
        return Result.done().data("list", sobs);
    }

    @ApiOperation("添加工资账套")
    @PostMapping("/add")
    public Result addSalarySob(@Valid @RequestBody Salary salary) {

        if (salaryService.checkName(salary)) {
            return Result.error().message("添加失败，账套名称【" + salary.getName() + "】已存在");
        }

        if (salaryService.save(salary)) {
            return Result.done().message("添加成功");
        }
        return Result.error().message("添加失败");
    }

    @ApiOperation("修改工资账套")
    @PostMapping("/update")
    public Result updateSalarySob(@Valid @RequestBody Salary salary) {

        if (salaryService.checkName(salary)) {
            return Result.error().message("修改失败，账套名称【" + salary.getName() + "】已存在");
        }

        if (salaryService.updateById(salary)) {
            return Result.done().message("修改成功");
        }
        return Result.error().message("修改失败");
    }

    @ApiOperation("批量删除工资账套")
    @PostMapping("/removeBatch")
    public Result removeBatchSalarySobs(@RequestBody List<Integer> ids) {

        if (salaryService.removeByIds(ids)) {
            return Result.done().message("删除成功");
        }
        return Result.error().message("删除失败");
    }
}

