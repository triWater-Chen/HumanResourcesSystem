package com.chen.myhr.service;

import com.chen.myhr.bean.HrRole;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.myhr.bean.vo.request.HrUpdateReq;

/**
 * @author Chen
 * @since 2021-07-28
 */
public interface HrRoleService extends IService<HrRole> {

    /**
     * 根据用户 id 更新所属所有角色
     * @param req 用户参数（只用到 id 和 List<Role>
     * @return boolean
     */
    boolean hrUpdateByRole(HrUpdateReq req);
}
