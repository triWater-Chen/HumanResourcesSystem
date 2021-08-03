package com.chen.myhr.controller;


import com.chen.myhr.bean.Joblevel;
import com.chen.myhr.bean.Position;
import com.chen.myhr.common.utils.Result;
import com.chen.myhr.service.JoblevelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Chen
 * @since 2021-07-28
 */
@Api(tags = {"职称管理"})
@RestController
@RequestMapping("/system/basic/jobLevel")
public class JobLevelController {

    @Resource
    JoblevelService joblevelService;

    @ApiOperation("获取所有职称")
    @GetMapping("/list")
    public Result getAllJobLevels() {

        List<Joblevel> levels = joblevelService.list();
        return Result.done().data("list", levels);
    }

    @ApiOperation("添加职称")
    @PostMapping("/add")
    public Result addJobLevels(@RequestBody Joblevel joblevel) {

        if (joblevelService.save(joblevel)) {
            return Result.done().message("添加成功");
        }
        return Result.error().message("添加失败");
    }

    @ApiOperation("修改职称")
    @PostMapping("/update")
    public Result updateJobLevels(@RequestBody Joblevel joblevel) {

        if (joblevelService.updateById(joblevel)) {
            return Result.done().message("修改成功");
        }
        return Result.error().message("修改失败");
    }

    @ApiOperation("批量删除职称")
    @PostMapping("/removeBatch")
    public Result removeBatchJobLevels(@RequestBody List<Integer> ids) {

        if (joblevelService.removeByIds(ids)) {
            return Result.done().message("删除成功");
        }
        return Result.error().message("删除失败");
    }
}

