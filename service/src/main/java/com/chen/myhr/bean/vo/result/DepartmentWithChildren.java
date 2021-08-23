package com.chen.myhr.bean.vo.result;

import com.chen.myhr.bean.Department;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author Chen
 * @description 封装查询部门树的返回参数
 * @create 2021-08-07
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value="DepartmentWithChildren参数", description="封装查询部门树的返回参数")
public class DepartmentWithChildren extends Department {

    private List<DepartmentWithChildren> children;
}
