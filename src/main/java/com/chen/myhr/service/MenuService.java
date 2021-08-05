package com.chen.myhr.service;

import com.chen.myhr.bean.Menu;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.myhr.bean.vo.result.MenuWithChildren;

import java.util.List;

/**
 * @author Chen
 * @since 2021-07-28
 */
public interface MenuService extends IService<Menu> {

    /**
     * 以当前登录用户 id 查询菜单
     * @return List<Menu>
     */
    List<Menu> getMenusByHrId();

    /**
     * 获取每个菜单及其所对应的角色
     * @return List<Menu>
     */
    List<Menu> getMenusWithRole();

    /**
     * 获取每个菜单及其字菜单，返回树形菜单
     * @return List<Menu>
     */
    List<MenuWithChildren> getMenusWithChildren();

}
