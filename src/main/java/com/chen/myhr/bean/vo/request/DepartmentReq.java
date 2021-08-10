package com.chen.myhr.bean.vo.request;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author Chen
 * @description 封装对部门进行查询的请求参数
 * @create 2021-08-07
 */
@Data
@ApiModel(value="DepartmentReq参数", description="封装对部门进行查询的请求参数")
public class DepartmentReq {

    private String name;

    private Boolean enabled;

    private String beginTime;

    private String endTime;
}
