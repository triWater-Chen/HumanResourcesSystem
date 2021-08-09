package com.chen.myhr.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chen.myhr.bean.Role;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.myhr.bean.vo.request.RolePageReq;
import com.chen.myhr.bean.vo.request.RoleUpdateReq;

import java.util.List;

/**
 * @author Chen
 * @since 2021-07-28
 */
public interface RoleService extends IService<Role> {

    /**
     * 检测角色英文名称是否重复
     * @param name 角色英文名
     * @return boolean
     */
    boolean checkRoleName(String name);

    /**
     * 按条件分页查询角色
     * @param req RolePageReq
     * @return List<Role>
     */
    Page<Role> listByCondition(RolePageReq req);

    /**
     * 添加/修改角色基本信息及其菜单权限
     * @param req RoleUpdateReq
     * @return boolean
     */
    boolean saveOrUpdateRoleWithMenu(RoleUpdateReq req);

    /**
     * 批量删除角色及其菜单权限
     * @param ids 角色 ids
     * @return boolean
     */
    boolean removeBatchRoleWithMenu(List<Integer> ids);
}
