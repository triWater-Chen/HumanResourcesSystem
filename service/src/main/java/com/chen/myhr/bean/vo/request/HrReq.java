package com.chen.myhr.bean.vo.request;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author Chen
 * @description 封装对用户进行查询的请求参数
 * @create 2021-08-11
 */
@Data
@ApiModel(value="HrReq参数", description="封装对用户进行查询的请求参数")
public class HrReq {

    private String name;

    private String username;

    private Boolean enabled;

    private String beginTime;

    private String endTime;
}
