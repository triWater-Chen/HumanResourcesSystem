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
     * 添加/修改角色的菜单权限
     * @param req 请求参数
     * @return boolean
     */
    boolean updateMenuByRole(RoleUpdateReq req);
}
