package com.chen.myhr.service;

import com.chen.myhr.bean.vo.request.HrReq;
import com.chen.myhr.bean.Hr;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.myhr.bean.Role;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

/**
 * @author Chen
 * @since 2021-07-28
 */
public interface HrService extends IService<Hr>, UserDetailsService {
    // 继承 UserDetailsService，重载 spring security 中对用户信息的查询功能

    /**
     * 检查用户名和手机号是否重复
     * @param hr 用户参数
     * @return boolean
     */
    boolean checkHrUsernameAndPhone(Hr hr);

    /**
     * 按条件查询用户，但排除查询 password 字段
     * @param req 用户参数
     * @return List<Hr>
     */
    List<Hr> listHrByCondition(HrReq req);

    /**
     * 根据 id 删除用户及其与角色的关联
     * @param id 用户 id
     * @return boolean
     */
    boolean removeHr(Integer id);

    /**
     * 根据用户 id 查询所属所有角色
     * @param id 用户 id
     * @return List<Role>
     */
    List<Role> getHrWithRole(Integer id);
}
