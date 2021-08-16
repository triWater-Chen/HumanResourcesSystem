package com.chen.myhr.bean.vo.request;

import com.chen.myhr.bean.vo.PageReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Chen
 * @description 封装对员工进行查询的带分页请求参数
 * @create 2021-08-16
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value="EmployeePageReq参数", description="封装对员工进行查询的带分页请求参数")
public class EmployeePageReq extends PageReq {

    private String name;

    @ApiModelProperty(value = "身份证号")
    private String idCard;

    @ApiModelProperty(value = "工号")
    private String workId;

    @ApiModelProperty(value = "职称ID")
    private Integer jobLevelId;

    @ApiModelProperty(value = "职位ID")
    private Integer posId;

    @ApiModelProperty(value = "部门ID")
    private Integer departmentId;

    @ApiModelProperty(value = "入职日期（初）")
    private String beginTime;

    @ApiModelProperty(value = "入职日期（末）")
    private String endTime;
}
