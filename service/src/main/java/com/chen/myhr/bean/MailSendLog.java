package com.chen.myhr.bean;

import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Chen
 * @since 2021-07-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="MailSendLog对象", description="发送邮箱记录")
public class MailSendLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("msgId")
    private String msgid;

    @TableField("empId")
    private Integer empid;

    @ApiModelProperty(value = "0发送中，1发送成功，2发送失败")
    private Integer status;

    @TableField("routeKey")
    private String routekey;

    private String exchange;

    @ApiModelProperty(value = "重试次数")
    private Integer count;

    @ApiModelProperty(value = "第一次重试时间")
    @TableField("tryTime")
    private Date trytime;

    @TableField("createTime")
    private Date createtime;

    @TableField("updateTime")
    private Date updatetime;


}
