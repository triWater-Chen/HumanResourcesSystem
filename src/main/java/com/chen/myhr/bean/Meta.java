package com.chen.myhr.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author Chen
 * @description 对菜单表的补充
 * @create 2021-07-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Meta对象", description="菜单表的拓展")
public class Meta implements Serializable {

    private Boolean keepAlive;

    @ApiModelProperty(value = "用于在前端判断是否需要权限访问（本项目未使用到，因为是在后端判断权限）")
    private Boolean requireAuth;
}
