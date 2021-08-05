package com.chen.myhr.bean.vo.request;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * @author Chen
 * @description 封装对角色进行添加、修改的请求参数
 * @create 2021-08-05
 */
@Data
@ApiModel(value="RoleUpdateReq参数", description="封装对角色进行添加、修改的请求参数")
public class RoleUpdateReq {

    private Integer id;

    private String name;

    private String namezh;

    private Boolean enabled;

    private List<Integer> menuIds;
}
