package com.chen.myhr.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Chen
 * @since 2021-07-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Appraise对象", description="考核")
public class Appraise implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer eid;

    @ApiModelProperty(value = "考评日期")
    @TableField("appDate")
    private Date appdate;

    @ApiModelProperty(value = "考评结果")
    @TableField("appResult")
    private String appresult;

    @ApiModelProperty(value = "考评内容")
    @TableField("appContent")
    private String appcontent;

    @ApiModelProperty(value = "备注")
    private String remark;


}
