package com.chen.myhr.common.controller;

import com.chen.myhr.bean.Menu;
import com.chen.myhr.common.utils.Result;
import com.chen.myhr.service.MenuService;
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
@RestController
@RequestMapping("/system/config")
public class SystemConfigController {

    @Resource
    MenuService menuService;

    @GetMapping("/menu")
    public Result testMenu() {

        List<Menu> menus = menuService.getMenusByHrId();
        return Result.done().data("list", menus);

    }
}
