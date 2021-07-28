package com.chen.myhr.common.controller;

import com.chen.myhr.common.utils.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Chen
 * @description 对 spring security 中的登录进行字符串提示，而不进行页面跳转
 * @create 2021-07-28
 */
@RestController
public class LoginController {

    @GetMapping("/login")
    public Result login() {
        return Result.error().message("尚未登录，请登录");
    }
}
