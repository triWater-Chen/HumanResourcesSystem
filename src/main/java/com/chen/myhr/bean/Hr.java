package com.chen.myhr.bean;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author Chen
 * @since 2021-07-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Hr对象", description="用户表")
public class Hr implements Serializable, UserDetails {
    // UserDetails 是 spring security 对用户信息的处理

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "hrID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "手机号码")
    private String phone;

    @ApiModelProperty(value = "住宅电话")
    private String telephone;

    @ApiModelProperty(value = "联系地址")
    private String address;

    private Boolean enabled;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "密码")
    private String password;

    @TableField("userFace")
    private String userFace;

    private String remark;

    @TableField(fill = FieldFill.INSERT, value = "createDate")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date createdate;

    @ApiModelProperty(value = "存放用户所具备的角色")
    @TableField(exist = false)
    private List<Role> roles;

    @ApiModelProperty(value = "查询并返回用户所具备的角色给 security")
    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 返回用户所具备的角色
            // 使用 @JsonIgnore，实现：
            // 在 json 序列化时将 pojo 中的一些属性忽略掉，标记在属性或者方法上，返回的 json 数据即不包含该属性

        List<SimpleGrantedAuthority> authorities = new ArrayList<>(roles.size());
        // 遍历添加该用户所具备的所有角色
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }

        return authorities;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        // 账户是否没过期
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        // 账户是否没锁定
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        // 密码是否没过期
        return true;
    }

    @Override
    public boolean isEnabled() {
        // 上面 3 个因为没有对应字段，直接默认为 true
        return enabled;
    }
}
