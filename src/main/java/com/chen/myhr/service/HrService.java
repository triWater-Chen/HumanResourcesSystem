package com.chen.myhr.service;

import com.chen.myhr.bean.Hr;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @author Chen
 * @since 2021-07-28
 */
public interface HrService extends IService<Hr>, UserDetailsService {
    // 继承 UserDetailsService，重载 spring security 中对用户信息的查询功能

}
