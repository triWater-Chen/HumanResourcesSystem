package com.chen.myhr.service.impl;

import com.chen.myhr.bean.Hr;
import com.chen.myhr.bean.Menu;
import com.chen.myhr.mapper.MenuMapper;
import com.chen.myhr.service.MenuService;
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

        Hr hr = (Hr) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer id = hr.getId();
        return baseMapper.getMenusByHrId(id);
    }

    @Override
    public List<Menu> getMenusWithRole() {
        return baseMapper.getMenusWithRole();
    }
}
