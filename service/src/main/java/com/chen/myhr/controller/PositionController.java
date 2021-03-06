package com.chen.myhr.controller;


import com.chen.myhr.bean.Position;
import com.chen.config.result.Result;
import com.chen.myhr.service.PositionService;
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
@Api(tags = {"职位管理"})
@RestController
@RequestMapping("/system/basic/position")
public class PositionController {

    @Resource
    PositionService positionService;

    @ApiOperation("获取所有职位")
    @GetMapping("/list")
    public Result getAllPositions() {

        List<Position> positions = positionService.list();
        return Result.done().data("list", positions);
    }

    @ApiOperation("添加职位")
    @PostMapping("/add")
    public Result addPositions(@Valid @RequestBody Position position) {

        if (positionService.checkPositionName(position.getName())) {
            return Result.error().message("添加失败，职位名称【" + position.getName() + "】已存在");
        }

        if (positionService.save(position)) {
            return Result.done().message("添加成功");
        }
        return Result.error().message("添加失败");
    }

    @ApiOperation("修改职位")
    @PostMapping("/update")
    public Result updatePositions(@Valid @RequestBody Position position) {

        // 此处使用数据库来判断字段重复（因为查询名称没排除被修改该行数据）

        if (positionService.updateById(position)) {
            return Result.done().message("修改成功");
        }
        return Result.error().message("修改失败");
    }

    @ApiOperation("删除职位（未使用）")
    @PostMapping("/remove/{id}")
    public Result removePositions(@PathVariable int id) {

        if (positionService.removeById(id)) {
            return Result.done().message("删除成功");
        }
        return Result.error().message("删除失败");
    }

    @ApiOperation("批量删除职位")
    @PostMapping("/removeBatch")
    public Result removeBatchPositions(@RequestBody List<Integer> ids) {

        if (positionService.removeByIds(ids)) {
            return Result.done().message("删除成功");
        }
        return Result.error().message("删除失败");
    }
}

