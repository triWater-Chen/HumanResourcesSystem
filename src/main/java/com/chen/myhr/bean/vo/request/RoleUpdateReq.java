package com.chen.myhr.bean.vo.request;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
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

    @Pattern(regexp = "^\\w{1,15}$", message = "【英文名称】格式不正确")
    private String name;

    @NotEmpty(message = "【中文名称】不能为空")
    private String namezh;

    private Boolean enabled;

    private List<Integer> menuIds;
}
