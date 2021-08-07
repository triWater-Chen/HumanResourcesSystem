package com.chen.myhr.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chen.myhr.bean.HrRole;
import com.chen.myhr.bean.MenuRole;
import com.chen.myhr.bean.Role;
import com.chen.myhr.bean.vo.request.RolePageReq;
import com.chen.myhr.bean.vo.request.RoleUpdateReq;
import com.chen.myhr.mapper.RoleMapper;
import com.chen.myhr.service.HrRoleService;
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

    @Resource
    HrRoleService hrRoleService;

    @Override
    public Page<Role> listByCondition(RolePageReq req) {

        QueryWrapper<Role> roleQueryWrapper = new QueryWrapper<>();

        // 动态 sql 查询
        if (StringUtils.hasLength(req.getName())) {
            roleQueryWrapper.like("name", "ROLE_" + req.getName());
        }
        if (StringUtils.hasLength(req.getNamezh())) {
            roleQueryWrapper.like("nameZh", req.getNamezh());
        }
        if (!ObjectUtils.isEmpty(req.getEnabled())) {
            roleQueryWrapper.eq("enabled", req.getEnabled());
        }
        if (StringUtils.hasLength(req.getBeginTime())) {
            roleQueryWrapper.ge("createDate", req.getBeginTime() + " 00:00:00");
        }
        if (StringUtils.hasLength(req.getEndTime())) {
            roleQueryWrapper.le("createDate", req.getEndTime() + " 23:59:59");
        }
        roleQueryWrapper.orderByAsc("id");

        Page<Role> page = new Page<>(req.getCurrent(), req.getSize());
        return page(page, roleQueryWrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveOrUpdateRoleWithMenu(RoleUpdateReq req) {

        // 添加/修改角色基本信息
        Role role = new Role();
        BeanUtils.copyProperties(req, role, "menuIds");
        role.setName("ROLE_" + req.getName());

        // 通过判断 role 是否存在 id 来进行添加或修改
        int update = 0;
        if (ObjectUtils.isEmpty(req.getId())) {
            update = baseMapper.insert(role);
        } else {
            // 根据判断 role 是否有主键来进行修改
            update = baseMapper.updateById(role);
        }

        // 对角色的菜单权限进行添加或修改
        // 若是添加，则必须先添加角色，才能获取 id，修改菜单权限，所以该步骤放最后
        boolean result = menuRoleService.saveOrUpdateMenuByRole(req);

        return update > 0 && result;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean removeBatchRoleWithMenu(List<Integer> ids) {
        // 因为有外键，需先清空相关子表，父表中才能进行删除

        // 先通过批量 rid 将 menu_role 表中相关数据批量删除（批量删除相关角色所属的菜单权限）
        menuRoleService.getBaseMapper()
                .delete(new QueryWrapper<MenuRole>().in("rid", ids));

        // 再通过批量 rid 将 hr_role 表中相关数据批量删除（批量删除相关用户所属的相关角色）
        hrRoleService.getBaseMapper()
                .delete(new QueryWrapper<HrRole>().in("rid", ids));

        // 最后批量删除 role 表中角色
        int deleteRoles = baseMapper.deleteBatchIds(ids);

        return deleteRoles > 0;
    }
}
