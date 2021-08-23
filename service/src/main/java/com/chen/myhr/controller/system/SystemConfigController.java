package com.chen.myhr.controller.system;

import com.chen.myhr.service.MenuService;
import com.chen.myhr.bean.Menu;
import com.chen.config.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Chen
 * @description 获取当前登录用户的菜单
 * @create 2021-07-29
 */
@Api(tags = {"系统设置"})
@RestController
@RequestMapping("/system")
public class SystemConfigController {

    @Resource
    MenuService menuService;

    @ApiOperation("获取所有菜单")
    @GetMapping("/menu")
    public Result testMenu() {

        List<Menu> menus = menuService.getMenusByHrId();
        return Result.done().data("menus", menus);

    }
}
