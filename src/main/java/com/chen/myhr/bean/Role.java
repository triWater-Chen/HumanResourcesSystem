package com.chen.myhr.bean;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;

/**
 * @author Chen
 * @since 2021-07-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Role对象", description="角色")
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @NotEmpty(message = "【角色英文名称】不能为空")
    private String name;

    @NotEmpty(message = "【角色中文名称】不能为空")
    @ApiModelProperty(value = "角色名称")
    @TableField("nameZh")
    private String namezh;

    @TableField(fill = FieldFill.INSERT, value = "createDate")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date createdate;

    private Boolean enabled;


}
