package com.chen.myhr.mapper;

import com.chen.myhr.bean.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * @author Chen
 * @since 2021-07-28
 */
public interface MenuMapper extends BaseMapper<Menu> {

    /**
     * 以当前登录用户 id 查询菜单
     * @param hrId 用户 id
     * @return List<Menu>
     */
    List<Menu> getMenusByHrId(Integer hrId);

    /**
     * 获取每个菜单及其所对应的角色
     * @return List<Role>
     */
    List<Menu> getMenusWithRole();
}
