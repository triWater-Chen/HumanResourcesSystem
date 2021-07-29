package com.chen.myhr.service;

import com.chen.myhr.bean.Menu;
import com.baomidou.mybatisplus.extension.service.IService;

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

}
