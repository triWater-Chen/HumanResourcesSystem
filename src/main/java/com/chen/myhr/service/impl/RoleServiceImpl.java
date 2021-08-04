package com.chen.myhr.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chen.myhr.bean.Role;
import com.chen.myhr.bean.vo.request.RolePageReq;
import com.chen.myhr.mapper.RoleMapper;
import com.chen.myhr.service.RoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author Chen
 * @since 2021-07-28
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Override
    public Page<Role> listByCondition(RolePageReq req) {

        QueryWrapper<Role> roleQueryWrapper = new QueryWrapper<>();

        // 动态 sql 查询
        if (StringUtils.hasLength((req.getName()))) {
            roleQueryWrapper.like("name", "ROLE_" + req.getName());
        }
        if (StringUtils.hasLength((req.getNamezh()))) {
            roleQueryWrapper.like("nameZh", req.getNamezh());
        }
        if (!ObjectUtils.isEmpty(req.getEnabled())) {
            roleQueryWrapper.eq("enabled", req.getEnabled());
        }
        roleQueryWrapper.orderByAsc("id");

        Page<Role> page = new Page<>(req.getCurrent(), req.getSize());
        return page(page, roleQueryWrapper);
    }
}
