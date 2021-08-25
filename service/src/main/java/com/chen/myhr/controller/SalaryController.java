package com.chen.myhr.controller;

import com.chen.config.result.Result;
import com.chen.myhr.bean.Salary;
import com.chen.myhr.service.SalaryService;
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
@Api(tags = {"工资账套管理"})
@RestController
@RequestMapping("/salary/sob")
public class SalaryController {

    @Resource
    SalaryService salaryService;

    @ApiOperation("获取所有职称")
    @GetMapping("/list")
    public Result getAllSalarySobs() {

        List<Salary> sobs = salaryService.list();
        return Result.done().data("list", sobs);
    }
}

