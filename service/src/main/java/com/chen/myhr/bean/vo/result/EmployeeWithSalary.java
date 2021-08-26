package com.chen.myhr.bean.vo.result;

import com.chen.myhr.bean.Department;
import com.chen.myhr.bean.Salary;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Chen
 * @description 封装查询员工账套的返回参数
 * @create 2021-08-26
 */
@Data
@ApiModel(value="DepartmentWithChildren参数", description="封装查询部门树的返回参数")
public class EmployeeWithSalary implements Serializable {

    @ApiModelProperty(value = "id")
    private Integer id;;

    @ApiModelProperty(value = "员工名")
    private String name;

    @ApiModelProperty(value = "工号")
    private String workId;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "电话号码")
    private String phone;

    @ApiModelProperty(value = "所属部门")
    private Department department;

    @ApiModelProperty(value = "所属账套")
    private Salary salary;
}
