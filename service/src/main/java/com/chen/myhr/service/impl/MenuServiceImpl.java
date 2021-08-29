package com.chen.myhr.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chen.myhr.mapper.MenuMapper;
import com.chen.myhr.service.MenuService;
import com.chen.myhr.bean.Hr;
import com.chen.myhr.bean.Menu;
import com.chen.myhr.bean.vo.result.MenuWithChildren;
import com.chen.utils.CopyUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Chen
 * @since 2021-07-28
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Override
    public List<Menu> getMenusByHrId() {
        // 使用自己写的 xml 中的方法

        Hr hr = (Hr) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer id = hr.getId();
        return baseMapper.getMenusByHrId(id);
    }

    @Override
    public List<Menu> getMenusWithRole() {
        // 使用自己写的 xml 中的方法
        return baseMapper.getMenusWithRole();
    }

    @Override
    public List<MenuWithChildren> getMenusWithChildren() {

        QueryWrapper<Menu> menuQueryWrapper = new QueryWrapper<>();
        menuQueryWrapper.eq("parentId", 1)
                .eq("enabled", 1);
        // 得到一级菜单
        List<Menu> menus = baseMapper.selectList(menuQueryWrapper);
        // 对得到的菜单进行封装
        List<MenuWithChildren> menuList = CopyUtil.copyList(menus, MenuWithChildren.class);

        // 遍历查询子菜单
        if (!menus.isEmpty()) {
            menuList.forEach(this :: findAllChild);
        }

        return menuList;
    }

    /**
     * 实现递归查询
     */
    public void findAllChild(MenuWithChildren menuList) {

        // 查出下一级菜单
        QueryWrapper<Menu> menuQueryWrapper = new QueryWrapper<>();
        menuQueryWrapper.eq("parentId", menuList.getId())
                .eq("enabled", 1)
                .orderByAsc("id");
        List<Menu> menus = baseMapper.selectList(menuQueryWrapper);

        // 对得到的菜单进行封装
        List<MenuWithChildren> menuChildren = CopyUtil.copyList(menus, MenuWithChildren.class);

        // 递归查询子菜单
        if (!menus.isEmpty()) {
            // 若非空，则将得到的菜单放进父菜单的 list 中，再进行递归查询

            menuList.setChildren(menuChildren);
            menuChildren.forEach(this :: findAllChild);
        }

    }
}
