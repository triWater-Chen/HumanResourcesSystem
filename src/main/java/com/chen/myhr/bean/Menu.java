package com.chen.myhr.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableName
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Chen
 * @since 2021-07-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Menu对象", description="菜单")
@TableName(resultMap = "menuResultMap")
public class Menu implements Serializable {
    // 使用 TableName 来为 resultMap 定义名字，方便在 xml 中自己整合映射关系

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String url;

    private String path;

    private String component;

    private String name;

    @TableField("iconCls")
    private String iconcls;
    
    @TableField("parentId")
    private Integer parentid;

    private Boolean enabled;

    // ----- 自定义字段 -----

    @TableField(exist = false)
    private Meta meta;

    @TableField(exist = false)
    private List<Menu> children;

    @TableField(exist = false)
    private List<Role> roles;

}
