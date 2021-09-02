package com.chen.myhr.controller.system;

import com.chen.myhr.bean.Hr;
import com.chen.myhr.bean.vo.request.ChatMessageReq;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author Chen
 * @description Websocket 服务
 * @create 2021-09-02
 */
@Controller
public class WebSocketController {

    @Resource
    SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/ws/chat")
    public void webSocketMessage(Authentication authentication, ChatMessageReq req) {

        Hr hr = (Hr) authentication.getPrincipal();
        req.setFrom(hr.getUsername());
        req.setFromNickName(hr.getName());
        req.setDate(new Date());
        simpMessagingTemplate.convertAndSendToUser(req.getTo(), "/queue/chat", req);
    }
}
