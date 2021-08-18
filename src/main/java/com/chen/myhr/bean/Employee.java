package com.chen.myhr.bean;

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

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * @author Chen
 * @since 2021-07-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Employee对象", description="员工详情")
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "员工编号")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "员工姓名")
    @NotEmpty(message = "【姓名】不能为空")
    private String name;

    @ApiModelProperty(value = "性别")
    @NotEmpty(message = "【性别】不能为空")
    private String gender;

    @ApiModelProperty(value = "出生日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private Date birthday;

    @ApiModelProperty(value = "身份证号")
    @Pattern(regexp = "(^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{2}$)", message = "【身份证号】格式不正确")
    @TableField("idCard")
    private String idCard;

    @ApiModelProperty(value = "婚姻状况")
    private String wedlock;

    @ApiModelProperty(value = "民族")
    @TableField("nationId")
    private Integer nationId;

    @ApiModelProperty(value = "籍贯")
    @TableField("nativePlace")
    private String nativePlace;

    @ApiModelProperty(value = "政治面貌")
    @TableField("politicId")
    private Integer politicId;

    @ApiModelProperty(value = "邮箱")
    @Email
    private String email;

    @ApiModelProperty(value = "电话号码")
    @Pattern(regexp = "^[1](([3][0-9])|([4][5-9])|([5][0-3,5-9])|([6][5,6])|([7][0-8])|([8][0-9])|([9][1,8,9]))[0-9]{8}$", message = "【电话号码】格式不正确")
    private String phone;

    @ApiModelProperty(value = "联系地址")
    private String address;

    @ApiModelProperty(value = "所属部门")
    @TableField("departmentId")
    private Integer departmentId;

    @ApiModelProperty(value = "职称ID")
    @TableField("jobLevelId")
    private Integer jobLevelId;

    @ApiModelProperty(value = "职位ID")
    @TableField("posId")
    private Integer posId;

    @ApiModelProperty(value = "聘用形式")
    @TableField("engageForm")
    private String engageForm;

    @ApiModelProperty(value = "最高学历")
    @TableField("tiptopDegree")
    private String tiptopDegree;

    @ApiModelProperty(value = "所属专业")
    private String specialty;

    @ApiModelProperty(value = "毕业院校")
    private String school;

    @ApiModelProperty(value = "入职日期")
    @TableField("beginDate")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private Date beginDate;

    @ApiModelProperty(value = "在职状态")
    @TableField("workState")
    private String workState;

    @ApiModelProperty(value = "工号")
    @TableField("workID")
    private String workId;

    @ApiModelProperty(value = "合同期限")
    @TableField("contractTerm")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private Double contractTerm;

    @ApiModelProperty(value = "转正日期")
    @TableField("conversionTime")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private Date conversionTime;

    @ApiModelProperty(value = "离职日期")
    @TableField("notWorkDate")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private Date notworkDate;

    @ApiModelProperty(value = "合同起始日期")
    @TableField("beginContract")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private Date beginContract;

    @ApiModelProperty(value = "合同终止日期")
    @TableField("endContract")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private Date endContract;

    @ApiModelProperty(value = "存放用户所具备的国籍")
    @TableField(exist = false)
    private Nation nation;

    @ApiModelProperty(value = "存放用户所具备的政治身份")
    @TableField(exist = false)
    private Politicsstatus politicsStatus;

    @ApiModelProperty(value = "存放用户所具备的部门")
    @TableField(exist = false)
    private Department department;

    @ApiModelProperty(value = "存放用户所具备的职称")
    @TableField(exist = false)
    private Joblevel jobLevel;

    @ApiModelProperty(value = "存放用户所具备的职位")
    @TableField(exist = false)
    private Position position;

}
