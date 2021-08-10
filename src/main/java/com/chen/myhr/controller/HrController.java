package com.chen.myhr.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chen.myhr.bean.Hr;
import com.chen.myhr.common.utils.Result;
import com.chen.myhr.service.HrService;
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
@Api(tags = {"用户管理"})
@RestController
@RequestMapping("/system/hr")
public class HrController {

    @Resource
    HrService hrService;

    @ApiOperation("查询所有用户（不返回密码）")
    @GetMapping("/list")
    public Result getAllHrsWithRole() {

        // 查询所有用户，但排除查询 password 字段
        // !"password".equals(i.getColumn()) 也可以实现
        List<Hr> hrs = hrService.getBaseMapper()
                .selectList(new QueryWrapper<Hr>().select(Hr.class, i -> !"password".equals(i.getProperty())));
        return Result.done().data("list", hrs);
    }
}

