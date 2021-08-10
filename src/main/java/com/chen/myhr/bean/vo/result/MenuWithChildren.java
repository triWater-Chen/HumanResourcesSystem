package com.chen.myhr.bean.vo.result;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * @author Chen
 * @description 封装查询菜单及其子菜单的返回参数
 * @create 2021-08-05
 */
@Data
@ApiModel(value="MenuWithChildren参数", description="封装查询菜单及其子菜单的返回参数")
public class MenuWithChildren {

    private Integer id;

    private String name;

    private List<MenuWithChildren> children;
}
