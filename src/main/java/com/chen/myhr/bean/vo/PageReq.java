package com.chen.myhr.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Chen
 * @description 封装分页请求参数
 * @create 2021-08-04
 */
@Data
@ApiModel(value="PageReq对象", description="封装分页请求参数")
public class PageReq {

    @ApiModelProperty(value = "当前页")
    private int current;

    @ApiModelProperty(value = "页面大小")
    private int size;
}
