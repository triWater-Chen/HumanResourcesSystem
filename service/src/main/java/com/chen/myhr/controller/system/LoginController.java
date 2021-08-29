package com.chen.myhr.controller.system;

import org.springframework.web.bind.annotation.RestController;

/**
 * @author Chen
 * @description 对 spring security 中的登录进行字符串提示，而不进行页面跳转
 * @create 2021-07-28
 */
@RestController
public class LoginController {

    /*
    @GetMapping("/login")
    public Result login() {
        // 配置表单登录页，使其返回 json 数据
        return Result.error().message("尚未登录，请登录");
    }*/
}
