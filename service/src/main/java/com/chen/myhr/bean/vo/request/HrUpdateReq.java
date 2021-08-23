package com.chen.myhr.bean.vo.request;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * @author Chen
 * @description 封装对用户进行添加、修改的请求参数
 * @create 2021-08-14
 */
@Data
@ApiModel(value="HrUpdateReq参数", description="封装对用户进行添加、修改的请求参数")
public class HrUpdateReq {

    private Integer id;

    private List<Integer> roleIds;
}
