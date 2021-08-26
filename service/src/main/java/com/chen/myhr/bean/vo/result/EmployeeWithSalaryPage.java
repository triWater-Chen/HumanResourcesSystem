package com.chen.myhr.bean.vo.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author Chen
 * @description 将查询员工账套的返回参数封装为分页数据
 * @create 2021-08-26
 */
@Data
@ApiModel(value="DepartmentWithChildren参数", description="封装查询部门树的返回参数")
public class EmployeeWithSalaryPage {

    @ApiModelProperty(value = "总页数")
    private Long total;

    @ApiModelProperty(value = "员工账套列表")
    private List<EmployeeWithSalary> list;
}
