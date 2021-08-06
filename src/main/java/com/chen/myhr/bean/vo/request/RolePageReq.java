package com.chen.myhr.bean.vo.request;

import com.chen.myhr.bean.vo.PageReq;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author Chen
 * @description 封装对角色进行查询的带分页请求参数
 * @create 2021-08-04
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value="RolePage参数", description="封装对角色进行查询的带分页请求参数")
public class RolePageReq extends PageReq{

    private Integer id;

    private String name;

    private String namezh;

    private Date createdate;

    private Boolean enabled;
}
