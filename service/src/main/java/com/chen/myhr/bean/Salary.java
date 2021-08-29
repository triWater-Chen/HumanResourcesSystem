package com.chen.myhr.bean;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author Chen
 * @since 2021-07-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Salary对象", description="工资详情")
public class Salary implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "基本工资")
    @TableField("basicSalary")
    @NotNull(message = "【基本工资】不能为空")
    private Integer basicSalary;

    @ApiModelProperty(value = "奖金")
    @NotNull(message = "【奖金】不能为空")
    private Integer bonus;

    @ApiModelProperty(value = "午餐补助")
    @TableField("lunchSalary")
    @NotNull(message = "【午餐补助】不能为空")
    private Integer lunchSalary;

    @ApiModelProperty(value = "交通补助")
    @TableField("trafficSalary")
    @NotNull(message = "【交通补助】不能为空")
    private Integer trafficSalary;

    @ApiModelProperty(value = "应发工资")
    @TableField("allSalary")
    private Integer allSalary;

    @ApiModelProperty(value = "养老金基数")
    @TableField("pensionBase")
    @NotNull(message = "【养老金基数】不能为空")
    private Integer pensionBase;

    @ApiModelProperty(value = "养老金比率")
    @TableField("pensionPer")
    @NotNull(message = "【养老金比率】不能为空")
    private Float pensionPer;

    @ApiModelProperty(value = "启用时间")
    @TableField(fill = FieldFill.INSERT, value = "createDate")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date createdate;

    @ApiModelProperty(value = "医疗基数")
    @TableField("medicalBase")
    @NotNull(message = "【医疗基数】不能为空")
    private Integer medicalBase;

    @ApiModelProperty(value = "医疗保险比率")
    @TableField("medicalPer")
    @NotNull(message = "【医疗保险比率】不能为空")
    private Float medicalPer;

    @ApiModelProperty(value = "公积金基数")
    @TableField("accumulationFundBase")
    @NotNull(message = "【公积金基数】不能为空")
    private Integer accumulationFundBase;

    @ApiModelProperty(value = "公积金比率")
    @TableField("accumulationFundPer")
    @NotNull(message = "【公积金比率】不能为空")
    private Float accumulationFundPer;

    @NotEmpty(message = "【账套名称】不能为空")
    private String name;


}
