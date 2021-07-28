package com.chen.myhr.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.Collection;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
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

    private String userface;

    private String remark;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        // 账户是否没过期
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // 账户是否没锁定
        return true;
    }

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
