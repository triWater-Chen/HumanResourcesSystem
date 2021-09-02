package com.chen.myhr.bean.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author Chen
 * @description 封装 websocket 消息
 * @create 2021-09-02
 */
@Data
@ApiModel(value="ChatMessageReq对象", description="封装 websocket 消息")
public class ChatMessageReq {

    @ApiModelProperty(value = "消息发送者名称")
    private String from;

    @ApiModelProperty(value = "消息发送者昵称")
    private String fromNickName;

    @ApiModelProperty(value = "消息去向")
    private String to;

    @ApiModelProperty(value = "消息内容")
    private String content;

    @ApiModelProperty(value = "消息时间")
    private Date date;
}
