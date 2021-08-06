package com.chen.myhr.service;

import com.chen.myhr.bean.MenuRole;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.myhr.bean.vo.request.RoleUpdateReq;

/**
 * @author Chen
 * @since 2021-07-28
 */
public interface MenuRoleService extends IService<MenuRole> {

    /**
     * 修改角色的菜单权限
     * @param req RoleUpdateReq
     * @return boolean
     */
    boolean updateMenuByRole(RoleUpdateReq req);
}
