package com.chen.myhr.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.chen.myhr.common.utils.poi.Excel;
import com.chen.myhr.common.utils.poi.Excels;
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

    @Excel(name = "员工编号", cellType = Excel.ColumnType.NUMERIC, prompt = "员工编号")
    @ApiModelProperty(value = "员工编号")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Excel(name = "姓名")
    @ApiModelProperty(value = "员工姓名")
    @NotEmpty(message = "【姓名】不能为空")
    private String name;

    @Excel(name = "工号", type = Excel.Type.EXPORT)
    @ApiModelProperty(value = "工号")
    @TableField("workID")
    private String workId;

    @Excel(name = "性别", combo = {"男", "女"})
    @ApiModelProperty(value = "性别")
    @NotEmpty(message = "【性别】不能为空")
    private String gender;

    @Excel(name = "电话号码", width = 30)
    @ApiModelProperty(value = "电话号码")
    @Pattern(regexp = "^[1](([3][0-9])|([4][5-9])|([5][0-3,5-9])|([6][5,6])|([7][0-8])|([8][0-9])|([9][1,8,9]))[0-9]{8}$", message = "【电话号码】格式不正确")
    private String phone;

    @Excel(name = "身份证号", width = 30)
    @ApiModelProperty(value = "身份证号")
    @Pattern(regexp = "(^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{2}$)", message = "【身份证号】格式不正确")
    @TableField("idCard")
    private String idCard;

    @Excel(name = "邮箱", width = 30)
    @ApiModelProperty(value = "邮箱")
    @Email(message = "邮箱格式错误")
    private String email;

    @Excel(name = "出生日期", width = 20, dateFormat = "yyyy-MM-dd")
    @ApiModelProperty(value = "出生日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private Date birthday;

    @Excel(name = "婚姻状况", combo = {"已婚", "未婚", "离异"})
    @ApiModelProperty(value = "婚姻状况")
    private String wedlock;

    @Excel(name = "民族编号", type = Excel.Type.IMPORT)
    @ApiModelProperty(value = "民族")
    @TableField("nationId")
    private Integer nationId;

    @Excels({
            @Excel(name = "民族", targetAttr = "name", type = Excel.Type.EXPORT)
    })
    @ApiModelProperty(value = "存放员工所具备的民族")
    @TableField(exist = false)
    private Nation nation;

    @Excel(name = "籍贯")
    @ApiModelProperty(value = "籍贯")
    @TableField("nativePlace")
    private String nativePlace;

    @Excel(name = "政治面貌编号", type = Excel.Type.IMPORT)
    @ApiModelProperty(value = "政治面貌")
    @TableField("politicId")
    private Integer politicId;

    @Excels({
            @Excel(name = "政治身份", targetAttr = "name", type = Excel.Type.EXPORT)
    })
    @ApiModelProperty(value = "存放员工所具备的政治身份")
    @TableField(exist = false)
    private Politicsstatus politicsStatus;

    @Excel(name = "联系地址", width = 30)
    @ApiModelProperty(value = "联系地址")
    private String address;

    @Excel(name = "部门编号", type = Excel.Type.IMPORT)
    @ApiModelProperty(value = "所属部门")
    @TableField("departmentId")
    private Integer departmentId;

    @Excels({
            @Excel(name = "部门", targetAttr = "name", type = Excel.Type.EXPORT)
    })
    @ApiModelProperty(value = "存放员工所具备的部门")
    @TableField(exist = false)
    private Department department;

    @Excel(name = "职位编号", type = Excel.Type.IMPORT)
    @ApiModelProperty(value = "职位ID")
    @TableField("posId")
    private Integer posId;

    @Excels({
            @Excel(name = "职位", targetAttr = "name", type = Excel.Type.EXPORT)
    })
    @ApiModelProperty(value = "存放员工所具备的职位")
    @TableField(exist = false)
    private Position position;

    @Excel(name = "职称编号", type = Excel.Type.IMPORT)
    @ApiModelProperty(value = "职称ID")
    @TableField("jobLevelId")
    private Integer jobLevelId;

    @Excels({
            @Excel(name = "职称", targetAttr = "name", type = Excel.Type.EXPORT)
    })
    @ApiModelProperty(value = "存放员工所具备的职称")
    @TableField(exist = false)
    private Joblevel jobLevel;

    @Excel(name = "聘用形式", combo = {"劳动合同", "劳务合同"})
    @ApiModelProperty(value = "聘用形式")
    @TableField("engageForm")
    private String engageForm;

    @Excel(name = "最高学历", combo = {"博士", "硕士", "本科", "大专", "高中", "初中", "小学", "其他"})
    @ApiModelProperty(value = "最高学历")
    @TableField("tiptopDegree")
    private String tiptopDegree;

    @Excel(name = "所属专业")
    @ApiModelProperty(value = "所属专业")
    private String specialty;

    @Excel(name = "毕业院校")
    @ApiModelProperty(value = "毕业院校")
    private String school;

    @Excel(name = "入职日期", width = 20, dateFormat = "yyyy-MM-dd")
    @ApiModelProperty(value = "入职日期")
    @TableField("beginDate")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private Date beginDate;

    @Excel(name = "转正日期", width = 20, dateFormat = "yyyy-MM-dd")
    @ApiModelProperty(value = "转正日期")
    @TableField("conversionTime")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private Date conversionTime;

    @Excel(name = "合同起始日期", width = 20, dateFormat = "yyyy-MM-dd")
    @ApiModelProperty(value = "合同起始日期")
    @TableField("beginContract")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private Date beginContract;

    @Excel(name = "合同终止日期", width = 20, dateFormat = "yyyy-MM-dd")
    @ApiModelProperty(value = "合同终止日期")
    @TableField("endContract")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private Date endContract;

    @Excel(name = "合同期限", cellType = Excel.ColumnType.NUMERIC, type = Excel.Type.EXPORT)
    @ApiModelProperty(value = "合同期限")
    @TableField("contractTerm")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private Double contractTerm;

    @Excel(name = "在职状态", combo = {"在职", "离职"})
    @ApiModelProperty(value = "在职状态")
    @TableField("workState")
    private String workState;

    @Excel(name = "离职日期", width = 20, dateFormat = "yyyy-MM-dd")
    @ApiModelProperty(value = "离职日期")
    @TableField("notWorkDate")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private Date notworkDate;

}
