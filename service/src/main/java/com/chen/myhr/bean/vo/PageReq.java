package com.chen.myhr.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

/**
 * @author Chen
 * @description 封装分页请求参数
 * @create 2021-08-04
 */
@Data
@ApiModel(value="PageReq对象", description="封装分页请求参数")
public class PageReq {

    @NotNull(message = "【当前页】不能为空")
    @ApiModelProperty(value = "当前页")
    private int current;

    @NotNull(message = "【页面大小】不能为空")
    @Max(value = 1000, message = "【每页条数】不能超过 1000 条")
    @ApiModelProperty(value = "页面大小")
    private int size;
}
