package com.chen.myhr.controller.system;

import com.chen.config.result.Result;
import com.chen.myhr.bean.Hr;
import com.chen.myhr.service.HrService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Chen
 * @description 查询聊天用户列表
 * @create 2021-09-01
 */
@RestController
@RequestMapping("/chat")
public class ChatController {

    @Resource
    HrService hrService;

    @GetMapping("/hrs")
    public Result getHrList() {

        List<Hr> hrsExceptCurrentHr = hrService.getHrsExceptCurrentHr();
        return Result.done().data("list", hrsExceptCurrentHr);
    }
}
