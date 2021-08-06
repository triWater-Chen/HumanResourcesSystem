package com.chen.myhr.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chen.myhr.bean.Role;
import com.chen.myhr.bean.vo.request.RolePageReq;
import com.chen.myhr.bean.vo.request.RoleUpdateReq;
import com.chen.myhr.mapper.RoleMapper;
import com.chen.myhr.service.MenuRoleService;
import com.chen.myhr.service.RoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Chen
 * @since 2021-07-28
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Resource
    MenuRoleService menuRoleService;

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

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateRoleWithMenu(RoleUpdateReq req) {

        // 对角色的菜单权限进行修改
        boolean result =  menuRoleService.updateMenuByRole(req);

        // 对角色的基本信息进行修改
        Role role = new Role();
        BeanUtils.copyProperties(req, role, "menuIds");
        String prefix = "ROLE_";
        if (req.getName().startsWith(prefix)) {
            role.setName(req.getName());
        } else {
            role.setName("ROLE_" + req.getName());
        }
        // 根据判断 role 是否有主键来进行修改
        int update = baseMapper.updateById(role);

        return result && update > 0;
    }
}
