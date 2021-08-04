package com.chen.myhr.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chen.myhr.bean.Role;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.myhr.bean.vo.request.RolePageReq;

import java.util.List;

/**
 * @author Chen
 * @since 2021-07-28
 */
public interface RoleService extends IService<Role> {

    /**
     * 按条件分页查询角色
     * @param req RolePageReq
     * @return List<Role>
     */
    Page<Role> listByCondition(RolePageReq req);
}
